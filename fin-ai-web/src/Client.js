import React, { Component } from 'react';

function Client(props) {

    if (props.applicationType === "Single"){
        return (
            <div className="flexbox-container" style={{textAlign: 'center', width: 1900, height: 150, border: 'solid'}}>
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
                <div className="vl"></div>
            </div>
        )
    }

    else if (props.applicationType === "Joint"){
        return (
            <div className="flexbox-container" style={{textAlign: 'center', width: 1900, height: 150, border: 'solid'}}>
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
                <div className="vl"></div>
                <p>Co-Applicant Firstname: {props.coApplicantFirstName}</p>
                <div className="vl"></div>
                <p>Co-Applicant Surname: {props.coApplicantSurname}</p>
                <div className="vl"></div>
                <p>Co-Applicant Email: {props.coApplicantEmail}</p>
                <div className="vl"></div>
                <p>Co-Applicant Address: {props.coApplicantAddress}</p>
                <div className="vl"></div>
                <p>Co-Applicant Gross Annual Income: {props.coApplicantGrossAnnualIncome}</p>
                <div className="vl"></div>
                <p>Co-Applicant PPS Number: {props.coApplicantPPSNumber}</p>
                <div className="vl"></div>
                <p>Co-Applicant Contact Number: {props.coApplicantContactNumber}</p>
                <div className="vl"></div>
                <p>Co-Applicant Date of Birth: {props.coApplicantDateOfBirth}</p>
            </div>
                )
    }
}

export default Client;