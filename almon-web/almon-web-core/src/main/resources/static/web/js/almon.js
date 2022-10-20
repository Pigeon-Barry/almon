function updateQueryParam(name, value) {
    if ('URLSearchParams' in window) {
        const searchParams = new URLSearchParams(window.location.search);
        searchParams.set(name, value);
        window.location.search = searchParams.toString();
    }
}

function toggleQueryParam(name) {
    if ('URLSearchParams' in window) {
        const searchParams = new URLSearchParams(window.location.search);
        if (searchParams.has(name))
            deleteQueryParam(name);
        else
            updateQueryParam(name, true);
    }
}

function deleteQueryParam(name) {
    if ('URLSearchParams' in window) {
        const searchParams = new URLSearchParams(window.location.search);
        searchParams.delete(name);
        window.location.search = searchParams.toString();
    }
}


function showAlertSuccess(message) {
    showAlert(message, "alert-success")
}

function showAlertError(message) {
    showAlert(message, "alert-danger")
}

function showAlertInfo(message) {
    showAlert(message, "alert-info")
}

function showAlert(message, alertType) {
    $('#alerts').append(
        '<div class="alert alert-dismissible fade show '.concat(alertType, '">', message, ' <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>')
    );
}

function confirmationPrompt(title, message) {
    return confirm(message);//TODO Make more pretty
}




$( document ).ready(function() {
    var popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'))
    var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
        return new bootstrap.Popover(popoverTriggerEl)
    })
});
