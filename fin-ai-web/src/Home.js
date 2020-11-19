import React from 'react';
import fire from './config/fire';
import ViewClientData from './ViewClientData.js';

class Home extends React.Component {

  logout() {
    fire.auth().signOut();
      document.getElementById("chatBotiFrame").style.display = "block";
  }

  state = {clientsVisible: false}

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
        <button onClick = {this.logout}>Logout</button>
      </div>
    )
  }
}
/* eslint-env jquery */

export default Home;
