# Load libraries
import random
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.model_selection import StratifiedKFold
from sklearn.metrics import accuracy_score
import joblib
from sklearn.linear_model import LogisticRegression
from flask import Flask, request
from flask_restful import Resource, Api
import warnings
import csv

warnings.filterwarnings("ignore")

app = Flask(__name__)
api = Api(app)


@app.route('/checkLoanStatus', methods=['POST'])
def get_result():
    request_json = request.get_json()
    request_json_loanID = request_json['loanId']
    result_csv = pd.read_csv('output.csv')
    results = result_csv['Loan_Status'][result_csv['Loan_ID'] == str(request_json_loanID)]
    return str(results)


@app.route('/submitCalculation', methods=['POST'])
def get_calculation_result():
    train = pd.read_csv('train.csv')
    test = pd.read_csv('test.csv')
    submission = pd.read_csv('sample_submission.csv')

    train_original = train.copy()
    test_original = test.copy()

    request_json = request.get_json()
    df = get_dataframe()
    loanId = "LP00" + str(random.randint(1000, 10000))
    i = 0
    while i < df['Loan_ID'].size:
        if loanId == df._get_value(i, 'Loan_ID'):
            loanId = "LP00" + str(random.randint(1000, 10000))
            i = 0
        else:
            i += 1
    gender = request_json['gender']
    married = request_json['married']
    dependents = request_json['dependents']
    education = request_json['education']
    selfEmployed = request_json['self-employed']
    applicantIncome = request_json['applicantIncome']
    coApplicantIncome = request_json['coApplicantIncome']
    loanAmount = request_json['loanAmount']
    loanAmountTerm = request_json['loanAmountTerm']
    creditHistory = request_json['creditHistory']
    propertyArea = request_json['propertyArea']

    # file = open('test.csv', mode='a')
    # writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    # writer.writerow([loanId,gender,married,dependents,education,selfEmployed,applicantIncome,coApplicantIncome,loanAmount,
    #               loanAmountTerm,creditHistory,propertyArea])
    # file.close()
    # file= open('sample_submission.csv', mode='a')
    # writer = csv.writer(file, delimiter=',', quotechar='"', quoting=csv.QUOTE_MINIMAL, lineterminator='\n')
    # writer.writerow([loanId,'N'])
    # file.close()

    # modifying test dataframe
    to_append = [loanId, gender, married, dependents, education, selfEmployed, applicantIncome, coApplicantIncome, loanAmount, loanAmountTerm, creditHistory, propertyArea]
    a_series = pd.Series(to_append, index=test_original.columns)
    test_original = test_original.append(a_series, ignore_index=True)
    # saving test dataframe with new modifications
    pd.DataFrame(test_original.to_csv('test.csv', index=False))

    # modifying sample_submission dataframe
    to_append = [loanId, 'N']
    a_series = pd.Series(to_append, index=submission.columns)
    submission = submission.append(a_series, ignore_index=True)
    # saving sample_submission.csv with new modifications
    pd.DataFrame(submission.to_csv('sample_submission.csv', index=False))

    load_dataset()
    result = get_submission_result(loanId)
    return result


# Load dataset
def load_dataset():
    train = pd.read_csv('train.csv')
    test = pd.read_csv('test.csv')

    train_original = train.copy()
    test_original = test.copy()

    print(train.shape)

    print(train['Loan_Status'].value_counts())

    print(train.isnull().sum())

    test['Gender'].fillna(train['Gender'].mode()[0], inplace=True)
    test['Married'].fillna(train['Married'].mode()[0], inplace=True)
    test['Dependents'].fillna(train['Dependents'].mode()[0], inplace=True)
    test['Self_Employed'].fillna(train['Self_Employed'].mode()[0], inplace=True)
    test['Credit_History'].fillna(train['Credit_History'].mode()[0], inplace=True)
    test['Loan_Amount_Term'].fillna(train['Loan_Amount_Term'].mode()[0], inplace=True)
    test['LoanAmount'].fillna(train['LoanAmount'].median(), inplace=True)

    train = train.drop('Loan_ID', axis=1)
    test = test.drop('Loan_ID', axis=1)

    train['Total_Income'] = train['ApplicantIncome'] + train['CoapplicantIncome']
    test['Total_Income'] = test['ApplicantIncome'] + test['CoapplicantIncome']

    train['EMI'] = train['LoanAmount'] / train['Loan_Amount_Term']
    test['EMI'] = test['LoanAmount'] / test['Loan_Amount_Term']

    train['Balance Income'] = train['Total_Income'] - (train['EMI'] * 1000)
    test['Balance Income'] = test['Total_Income'] - (test['EMI'] * 1000)

    X = train.drop('Loan_Status', 1)
    y = train.Loan_Status

    X = pd.get_dummies(X)
    train = pd.get_dummies(train)
    test = pd.get_dummies(test)

    x_train, x_cv, y_train, y_cv = train_test_split(X, y, test_size=0.3)

    model = LogisticRegression()
    model.fit(x_train, y_train)
    LogisticRegression(max_iter=1000)

    pred_cv = model.predict(x_cv)
    print(accuracy_score(y_cv, pred_cv))

    submission = pd.read_csv('sample_submission.csv')

    i = 1
    mean = 0
    kf = StratifiedKFold(n_splits=5)
    for train_index, test_index in kf.split(X, y):
        print('\n{} of kfold {} '.format(i, kf.n_splits))
        xtr, xvl = X.loc[train_index], X.loc[test_index]
        ytr, yvl = y[train_index], y[test_index]
        model = LogisticRegression(random_state=1)
        model.fit(xtr, ytr)
        pred_test = model.predict(xvl)
        score = accuracy_score(yvl, pred_test)
        mean += score
        print('accuracy_score', score)
        i += 1
        pred_test = model.predict(test)
        pred = model.predict_proba(xvl)[:, 1]
    print('\n Mean Validation Accuracy', mean / (i - 1))

    submission['Loan_Status'] = pred_test
    submission['Loan_ID'] = test_original['Loan_ID']
    submission['Loan_Status'].replace(0, 'N', inplace=True)
    submission['Loan_Status'].replace(1, 'Y', inplace=True)
    pd.DataFrame(submission, columns=['Loan_ID', 'Loan_Status']).to_csv('output.csv')


def get_dataframe():
    df = pd.read_csv('test.csv')
    return df


def get_submission_result(id):
    result_csv = pd.read_csv('output.csv')
    results = result_csv['Loan_Status'][result_csv['Loan_ID'] == id]
    resultList = results.tolist()
    if resultList[0] == "Y":
        return "Y"
    else:
        return "N"


if __name__ == '__main__':
    load_dataset()
    app.run(host="192.168.1.10", debug=True) # host has to be changed to your computers local ipv4 address (
    # cmd->ipconfig)
