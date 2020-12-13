import React from 'react';
import fire from './config/fire';
import ViewClientData from './ViewClientData.js';

class Home extends React.Component {

    state = {clientsVisible: false, exportDataBtn: false, exportDataStatus: ''}

  logout() {
    fire.auth().signOut();
      document.getElementById("chatBotiFrame").style.display = "block";
  }

    exportData = (e) =>{
      fetch('http://192.168.1.10:5000/exportData').then(response => response.text())
          .then(data => {
              console.log(data)
              this.setState({exportDataStatus: data})
      })
          .catch(e => {
              console.log(e)
          });
  }

  getUserRole() {
      if (fire.auth().currentUser.uid === "nKUaqqst1kejYEWQaDKDJ56YlYo1"){
          return <button onClick={this.exportData}>Export Client Data</button>
      }
      else {
      }
  }

  render() {
    return (
      <div style={{textAlign: 'center'}}>
          <script type="javascript">
              hideToggle(".chatBotiFrame")
          </script>
        <h1>You Are Logged In</h1>
          <div>
              {this.state.clientsVisible ? <ViewClientData /> : null}
              <button onClick={() => {
                  this.setState({clientsVisible: true})
              }}>View Client Data</button>
          </div>

          <div>
              {this.getUserRole()}
          </div>

          <p> {this.state.exportDataStatus}</p>

        <button onClick = {this.logout}>Logout</button>
      </div>
    )
  }
}
/* eslint-env jquery */

export default Home;
