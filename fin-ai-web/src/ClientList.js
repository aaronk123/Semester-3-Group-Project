import React, { Component } from 'react';
import Client from './Client';

function ClientList(props){
    let clients = props.filteredClients.map((client) => {
        return <Client title={client.title} firstname={client.firstname} surname={client.surname} email={client.email}
        applicationType={client.applicationType} grossAnnualIncome={client.grossAnnualIncome} ppsNumber={client.ppsNumber}
        phoneNumber={client.phoneNumber} address={client.address} dateOfBirth={client.dateofBirth} loanTerm={client.loanTerm}
        loanAmount={client.LoanAmount}/>
    })
    return (
        <div style={{ textAlign: 'center' }}>
            {clients}
        </div>
    )
}

export default ClientList;