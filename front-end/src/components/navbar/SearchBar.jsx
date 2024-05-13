import React from 'react';

const SearchBar = (props) => {
    return (
        <div>
            <input 
                className="searchBarInput"
                placeholder='Search for a movie...'
                value={props.search}
                onChange={(event) => props.setSearchVal(event.target.value)}>
            </input>
        </div>
    )
}

export default SearchBar;
