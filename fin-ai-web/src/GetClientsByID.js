import React from 'react';
import fire from './config/fire';
const db = fire.firestore();

class GetClientsByID extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            clients: []
        };
    }

    // This could be useful in making the JSON data appear more readable.
    jsonToSCSS(stringdata=``, data={}) {
        /* JSON.parse can throw. Always be ready for that. */
        try { data = JSON.parse(stringdata); }
        catch (e) { console.warn(e); return ``; }

        return Object.keys(data)
            .map(key => `${key}: ${data[key]};`)
            .join('\n');
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
                            stateYoke.setState({ clients : clients })
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

        return (
            <div style={{textAlign: 'center'}}>
                <script type="javascript">
                    hideToggle(".chatBotiFrame")
                </script>

                <div id='renderhere'>
                    <pre>{JSON.stringify(this.state.clients, null, 2)}</pre>
                </div>

            </div>
        )
    }
}

/* eslint-env jquery */

export default GetClientsByID;
