import React from 'react';
import fire from './config/fire';
import ClientList from './ClientList';
import FilterBox from './FilterBox';

const db = fire.firestore();

class GetClientsByID extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            clients: [],
            searchClient: ''
        };
    }

    handleInput = (e) => {
        this.setState({ searchClient: e.target.value })
    }


    componentDidMount() {
        this.getClients()
    }

    getClients(){
        let userID = fire.auth().currentUser.uid
        let empID = ""
        let stateYoke = this // should probably change the name to something else. Using "this" within db.collection res
        // ult doesnt work, I guess cause it is in the wrong scope, hence the need for stateYoke.

        const docRef = db.collection("users").doc(userID);
        docRef.get().then(function(doc) {
            if (doc.exists) {
                empID = doc.data()
                empID = empID.ID

                // get all clients with same empID as logged in professional
                db.collection("user_application_forms")
                    .where("ID", "==", empID)
                    .get()
                    .then(snapshot => {
                        snapshot.docs.forEach(doc => {
                            let clients = doc.data()

                            let clientTitle = ""
                            let clientFirstName = ""
                            let clientSurname = ""
                            let clientEmail = ""
                            let clientApplicationType = ""
                            let clientgrossAnnualIncome = ""
                            let clientppsNumber = ""
                            let clientphoneNumber = ""
                            let clientaddress = ""
                            let clientDateofBirth = ""
                            let clientLoanTerm = ""
                            let clientLoanAmount = ""

                            Object.keys(clients["title"]).map(key => clients["title"][key]).forEach(item => clientTitle = clientTitle + item);
                            Object.keys(clients["firstName"]).map(key => clients["firstName"][key]).forEach(item => clientFirstName = clientFirstName + item);
                            Object.keys(clients["surName"]).map(key => clients["surName"][key]).forEach(item => clientSurname = clientSurname + item);
                            Object.keys(clients["email"]).map(key => clients["email"][key]).forEach(item => clientEmail = clientEmail + item);
                            Object.keys(clients["applicationType"]).map(key => clients["applicationType"][key]).forEach(item => clientApplicationType = clientApplicationType + item);
                            Object.keys(clients["grossAnnualIncome"]).map(key => clients["grossAnnualIncome"][key]).forEach(item => clientgrossAnnualIncome = clientgrossAnnualIncome + item);
                            Object.keys(clients["ppsNumber"]).map(key => clients["ppsNumber"][key]).forEach(item => clientppsNumber = clientppsNumber + item);
                            Object.keys(clients["phoneNumber"]).map(key => clients["phoneNumber"][key]).forEach(item => clientphoneNumber = clientphoneNumber + item);
                            Object.keys(clients["address"]).map(key => clients["address"][key]).forEach(item => clientaddress = clientaddress + item);
                            Object.keys(clients["dateOfBirth"]).map(key => clients["dateOfBirth"][key]).forEach(item => clientDateofBirth = clientDateofBirth + item);
                            Object.keys(clients["loanTerm"]).map(key => clients["loanTerm"][key]).forEach(item => clientLoanTerm = clientLoanTerm + item);
                            Object.keys(clients["loanAmount"]).map(key => clients["loanAmount"][key]).forEach(item => clientLoanAmount = clientLoanAmount + item);



                            console.log(clientTitle, clientFirstName, clientSurname)

                            clients = [
                                {title: clientTitle, firstname: clientFirstName, surname: clientSurname,
                                    email: clientEmail, applicationType: clientApplicationType,
                                    grossAnnualIncome: clientgrossAnnualIncome, ppsNumber: clientppsNumber,
                                    phoneNumber: clientphoneNumber, address: clientaddress, dateofBirth: clientDateofBirth,
                                    loanTerm: clientLoanTerm, LoanAmount: clientLoanAmount}
                            ]

                            stateYoke.setState({ clients : stateYoke.state.clients.concat(clients)});

                        })
                    })
            } else {
                // doc.data() will be undefined in this case
                console.log("No such document!");
            }
        }).catch(function(error) {
            console.log("Error getting document:", error);
        });
    }

    render() {

        let filteredClients = this.state.clients.filter((client) => {
            return client.email.toLowerCase().includes(this.state.searchClient)
        })

        return (
            <div style={{textAlign: 'center'}}>
                <script type="javascript">
                    hideToggle(".chatBotiFrame")
                </script>

                <div>
                    <h1>Filter client by Email</h1>
                    <FilterBox handleInput={this.handleInput}/>

                    <ClientList filteredClients={filteredClients}/>
                </div>

            </div>
        )
    }
}

/* eslint-env jquery */

export default GetClientsByID;
