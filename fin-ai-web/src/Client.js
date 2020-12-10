import React, { Component } from 'react';

function Client(props) {
    return (
        <div class="flexbox-container" style={{textAlign: 'center', width: 1000, height:100, border: 'solid'}}>
            <p>Title: {props.title}</p>

                <div className="vl"></div>
            <p>First name: {props.firstname}</p>
                <div className="vl"></div>
            <p>Surname: {props.surname}</p>
                <div className="vl"></div>
            <p>Email: {props.email}</p>
                <div className="vl"></div>
            <p>Application Type: {props.applicationType}</p>
                <div className="vl"></div>
            <p>Gross Annual Income: {props.grossAnnualIncome}</p>
                <div className="vl"></div>
            <p>PPS Number: {props.ppsNumber}</p>
                <div className="vl"></div>
            <p>Phone Number: {props.phoneNumber}</p>
                <div className="vl"></div>
            <p>Address: {props.address}</p>
                <div className="vl"></div>
            <p>Date of Birth: {props.dateOfBirth}</p>
                <div className="vl"></div>
            <p>Loan Term: {props.loanTerm}</p>
                <div className="vl"></div>
            <p>Loan Amount: {props.loanAmount}</p>
        </div>
    )
}

export default Client;