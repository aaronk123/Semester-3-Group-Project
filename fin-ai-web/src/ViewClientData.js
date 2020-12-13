import React from 'react';
import GetClientsByID from './GetClientsByID';

class ViewClientData extends React.Component {

    state = {displayClients: false}

    render() {
        return (
            <div style={{textAlign: 'center'}}>
                <script type="javascript">
                    hideToggle(".chatBotiFrame")
                </script>
                <h1>Viewing your clients</h1>
                <hr></hr>
                {this.state.displayClients ? <GetClientsByID /> : null}
                <button onClick={() => this.setState({displayClients: true})}>View my client details</button>
            </div>
        )
    }
}

/* eslint-env jquery */

export default ViewClientData;
