import React from 'react';
import fire from './config/fire';

class Home extends React.Component {

  logout() {
    fire.auth().signOut();
      document.getElementById("chatBotiFrame").style.display = "block";
  }

  render() {
    return (
      <div style={{textAlign: 'center'}}>
          <script type="javascript">
              hideToggle(".chatBotiFrame")
          </script>
        <h1>You Are Logged In</h1>
        <button onClick = {this.logout}>Logout</button>
      </div>
    )
  }
}
/* eslint-env jquery */

export default Home;