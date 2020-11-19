import React from 'react';
import fire from './config/fire';

class ViewClientData extends React.Component {

    render() {
        return (
            <div style={{textAlign: 'center'}}>
                <script type="javascript">
                    hideToggle(".chatBotiFrame")
                </script>
                <h1>This is another page</h1>
            </div>
        )
    }
}

/* eslint-env jquery */

export default ViewClientData;
