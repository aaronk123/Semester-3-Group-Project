import React from 'react';
import fire from './config/fire';

// const chatBot = document.getElementById("chatBotiFrame");
// const testBtn = document.getElementById("testBtn");

class Login extends React.Component {

  signUp() {

      document.getElementById("chatBotiFrame").style.display = "none";

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

      document.getElementById("chatBotiFrame").style.display = "none";

    const email = document.querySelector('#email').value;
    const password = document.querySelector('#password').value;
    // const employeeID = document.querySelector('#empID').value;
    fire.auth().signInWithEmailAndPassword(email, password)
      .then((u) => {

        if (fire.auth().currentUser.uid === "nKUaqqst1kejYEWQaDKDJ56YlYo1"){
            console.log("This user is an admin");
        }
        else{
            console.log("regular user");
        }

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
          <input id="password" placeholder="Enter Password.." type="password"/>
        </div>
         <div>Employee ID</div>
          <div>
              <input id="empID" placeholder="Enter employee ID.." type="text"/>
          </div>
        <button class="button1" style={{margin: '10px'}} onClick={this.login}>Login</button>
        <button class="button2"  style={{margin: '10px'}} onClick={this.signUp}>Sign Up</button>
      </div>
    )
  }
}

/* eslint-env jquery */

export default Login;
