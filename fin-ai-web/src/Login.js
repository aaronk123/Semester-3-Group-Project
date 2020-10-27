import React from 'react';
import fire from './config/fire';

class Login extends React.Component {

  signUp() {
    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;
    const employeeID = document.querySelector('#empID').value.trim();
    if (employeeID === null || employeeID.length === 0) {
        alert("You must enter an EmployeeID");
    } else {
        fire.auth().createUserWithEmailAndPassword(email, password)
            .then((u) => {
                const ref = fire.firestore().collection("users").doc(u.user.uid)
                const data = {
                    email: email,
                    ID: employeeID,
                };
                ref.set(data).then(r =>
                    console.log('Successfully Signed Up with user Id ' + u.user.uid)
                );
            })
            .catch((err) => {
                console.log('Error: ' + err.toString());
            })
    }
  }

  login() {
    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;
    const employeeID = document.querySelector('#empID').value;
    fire.auth().signInWithEmailAndPassword(email, password)
      .then((u) => {
        console.log('Successfully Logged In');
      })
      .catch((err) => {
        console.log('Error: ' + err.toString());
      })
  }

  render() {
    return (
      <div style={{ textAlign: 'center' }}>
        <div>
          <div>Email</div>
          <input id="email" placeholder="Enter Email.." type="text"/>
        </div>
        <div>
          <div>Password</div>
          <input id="password" placeholder="Enter Password.." type="text"/>
        </div>
         <div>Employee ID</div>
          <div>
              <input id="empID" placeholder="Enter employee ID.." type="text"/>
          </div>
        <button style={{margin: '10px'}} onClick={this.login}>Login</button>
        <button style={{margin: '10px'}} onClick={this.signUp}>Sign Up</button>
      </div>
    )
  }
}

export default Login;
