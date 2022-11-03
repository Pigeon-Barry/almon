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

function deleteQueryParam(paramNames) {
    if ('URLSearchParams' in window) {
        const searchParams = new URLSearchParams(window.location.search);
        console.log(paramNames);
        paramNames.forEach(function (name,index){
            console.log(name);
            searchParams.delete(name);
        });
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
function showPopup(title, message){
    return confirmationPrompt(title,message);
}

function redirect(url){
    window.location = url;
}
function refreshPage(){
    location.reload();
}


$( document ).ready(function() {
    const popoverTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="popover"]'));
    const popoverList = popoverTriggerList.map(function (popoverTrigger) {
        return new bootstrap.Popover(popoverTrigger)
    });
});

function formToJson(formId) {
    const elements = document.querySelectorAll('#' + formId + ' input');
    const data = {};
    for (let i = 0; i < elements.length; i++) {
        const el = elements[i];
        let val = el.value;
        if (!val) val = "";
        const fullName = el.getAttribute("name");
        if (!fullName) continue;
        const fullNameParts = fullName.split('.');
        let prefix = '';
        let stack = data;
        for (let k = 0; k < fullNameParts.length - 1; k++) {
            prefix = fullNameParts[k];
            if (!stack[prefix]) {
                stack[prefix] = {};
            }
            stack = stack[prefix];
        }
        prefix = fullNameParts[fullNameParts.length - 1];
        if (stack[prefix]) {
            const newVal = stack[prefix] + ',' + val;
            stack[prefix] += newVal;
        } else {
            stack[prefix] = val;
        }
    }
    return data;
}

$( document ).ready(function() {
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
});
