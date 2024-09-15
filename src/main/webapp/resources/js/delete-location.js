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
                // TODO: set label "no location found" if it aws last location
                let card =  delBtn.closest(".col");
                card.remove()
                console.log(`Location with id=${locationId} was removed successfully`)

            } else {
                console.error(`Failed to remove location with id=${locationId}`)
            }
        })

        .catch(error => {
            console.error('Error: ', error)
        })

}
