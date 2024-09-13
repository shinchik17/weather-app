function deleteLocation(element) {

    let appContext = element.getAttribute("data-ctx")
    let locationId = element.getAttribute("data-loc-id")

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
                console.log(`Location with id=${locationId} was removed succesfully`)
            } else {
                console.error(`Failed to remove location with id=${locationId}`)
            }
        })

        .catch(error => {
            console.error('Error: ', error)
        })

}