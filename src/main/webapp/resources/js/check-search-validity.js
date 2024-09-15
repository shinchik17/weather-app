function checkSearchValidity(){
    let input = document.getElementsByName("location-name")[0];
    input.addEventListener('invalid', event => {
        if (event.target.validity.valueMissing || event.target.validity.patternMismatch) {
            event.target.setCustomValidity('Location name must contain at least 1 non-space character');
        }
    })
    input.addEventListener('change', event => {
        event.target.setCustomValidity('');
    })
}

checkSearchValidity()


