import React from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart } from '@fortawesome/free-solid-svg-icons';

const AddFavourites = () => {
    return (
        <div>
            <span className='favorite-text'></span>
            <FontAwesomeIcon icon={faHeart} className="favorite-icon" />
        </div>
    );
}

export default AddFavourites;
