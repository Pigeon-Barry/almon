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

let alertId = 0;
function showAlert(message, alertType) {
    const idSuffix = alertId;
    alertId = alertId + 1;
    let newAlertId = "alert-" + idSuffix;
    $('#alerts').append(
        '<div id="' + newAlertId + '" class="alert alert-dismissible fade show '.concat(alertType, '">', message, ' <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button></div>')
    );
    $('#' + newAlertId).fadeTo(5000, 500).slideUp(500, function () {
        $(this).remove();
    });
}

let promptId = 0;

function confirmationPrompt(title, message, onConfirm, onCancel, onClose) {
    const idSuffix = promptId;
    promptId = promptId + 1;

    $('.modal:visible').attr("data-show-with-model-close", idSuffix);
    const visibleModels = $('.modal[data-show-with-model-close="' + idSuffix + '"]');
    visibleModels.hide();

    const val = "<div id=\"confirmationDialogModel_" + idSuffix + "\" class=\"modal fade\" data-bs-backdrop=\"static\" data-bs-keyboard=\"false\" tabindex=\"-1\"\n" +
        "                 aria-labelledby=\"confirmationDialogTitle_" + idSuffix + "\" aria-hidden=\"true\">\n" +
        "                <div class=\"modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable\">\n" +
        "                    <div class=\"modal-content\">\n" +
        "                        <div class=\"modal-header\">\n" +
        "                            <h5 class=\"modal-title\" id=\"confirmationDialogTitle_" + idSuffix + "\">" + title + "</h5>\n" +
        "                            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
        "                        </div>\n" +
        "                        <div class=\"modal-body\" id=\"confirmationDialogBody_" + idSuffix + "\">\n" +
        "                            <p id=\"confirmationDialogText\">" + message + "</p>\n" +
        "                        </div>\n" +
        "                        <div class=\"modal-footer\">\n" +
        "                            <button type=\"button\" class=\"btn btn-danger\" data-bs-dismiss=\"modal\" id=\"confirmationDialogCancel_" + idSuffix + "\">Cancel</button>\n" +
        "                            <button type=\"submit\" class=\"btn btn-primary\" id=\"confirmationDialogOK_" + idSuffix + "\">OK</button>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>";

    const promptContainer = $('#prompt-container');
    promptContainer.append(val);

    const modal = $('#confirmationDialogModel_' + idSuffix);
    const close = function () {
        if (onClose !== null && onClose !== undefined) {
            onClose();
        }
        visibleModels.show();
        modal.remove();
    }
    //Add new Listeners
    modal.on('hide.bs.modal', function (event) {
        if (onCancel !== null && onCancel !== undefined) {
            onCancel();
        }
        close();
    });
    $('#confirmationDialogOK_' + idSuffix).on('click', function (event) {
        modal.off('hide.bs.modal');
        modal.modal('hide');
        if (onConfirm !== null && onConfirm !== undefined) {
            onConfirm();
        }
        close();
    });
    modal.modal('show');
}

function showPopup(title, message, onConfirm, onCancel, onClose) {
    const idSuffix = promptId;
    promptId = promptId + 1;

    $('.modal:visible').attr("data-show-with-model-close", idSuffix);
    const visibleModels = $('.modal[data-show-with-model-close="' + idSuffix + '"]');
    visibleModels.hide();
    const val = "<div id=\"confirmationDialogModel_" + idSuffix + "\" class=\"modal fade\" data-bs-backdrop=\"static\" data-bs-keyboard=\"false\" tabindex=\"-1\"\n" +
        "                 aria-labelledby=\"confirmationDialogTitle_" + idSuffix + "\" aria-hidden=\"true\">\n" +
        "                <div class=\"modal-dialog modal-lg modal-dialog-centered modal-dialog-scrollable\">\n" +
        "                    <div class=\"modal-content\">\n" +
        "                        <div class=\"modal-header\">\n" +
        "                            <h5 class=\"modal-title\" id=\"confirmationDialogTitle_" + idSuffix + "\">" + title + "</h5>\n" +
        "                            <button type=\"button\" class=\"btn-close\" data-bs-dismiss=\"modal\" aria-label=\"Close\"></button>\n" +
        "                        </div>\n" +
        "                        <div class=\"modal-body\" id=\"confirmationDialogBody_" + idSuffix + "\">\n" +
        "                            <p id=\"confirmationDialogText\">" + message + "</p>\n" +
        "                        </div>\n" +
        "                        <div class=\"modal-footer\">\n" +
        "                            <button type=\"submit\" class=\"btn btn-primary\"  id=\"confirmationDialogOK_" + idSuffix + "\">OK</button>\n" +
        "                        </div>\n" +
        "                    </div>\n" +
        "                </div>\n" +
        "            </div>";

    const promptContainer = $('#prompt-container');
    promptContainer.append(val);

    const modal = $('#confirmationDialogModel_' + idSuffix);

    const close = function () {
        if (onClose !== null && onClose !== undefined) {
            onClose();
        }
        visibleModels.show();
        modal.modal('hide');
        modal.remove();
    }
    //Add new Listeners
    modal.on('hide.bs.modal', function (event) {
        if (onCancel !== null && onCancel !== undefined) {
            onCancel();
        }
        close();
    });
    $('#confirmationDialogOK_' + idSuffix).on('click', function (event) {
        modal.off('hide.bs.modal');
        if (onConfirm !== null && onConfirm !== undefined) {
            onConfirm();
        }
        close();
    });
    modal.modal('show');
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
    $('[data-bs-toggle="tooltip"]').tooltip({
        trigger : 'hover'
    });
    // const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]')
    // const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl))
});
