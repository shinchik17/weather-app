function deleteLocation(delBtn) {

    let appContext = delBtn.getAttribute("data-ctx")
    let locationId = delBtn.getAttribute("data-loc-id")

    fetch(`${appContext}`, {
        method: 'DELETE',
        headers: {
            'ContentType': 'application/json;utf-8',
        },
        body: JSON.stringify({
            location_id: `${locationId}` })
    })
        .then(response => {
            if (response.ok) {
                let card =  delBtn.closest(".col");
                card.remove()
                console.log(`Location with id=${locationId} was removed successfully`)

                if (document.querySelectorAll(".col").length === 0){
                    let header = document.querySelector("h1");
                    header.textContent = "No locations added";
                    header.closest(".container").hidden = false
                }

            } else {
                console.error(`Failed to remove location with id=${locationId}`)
            }
        })

        .catch(error => {
            console.error('Error: ', error)
        })

}
