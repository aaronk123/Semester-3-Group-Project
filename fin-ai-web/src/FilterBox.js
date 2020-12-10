import React, { Component } from 'react';

function FilterBox(props) {
    return (
        <div>

            <input id="filterText" onChange={props.handleInput} type="text"></input>

        </div>

    )
}

export default FilterBox