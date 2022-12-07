function disableService(serviceId, domElement) {
    updateEnableStatusService(domElement, serviceId, false);
}

function enableService(serviceId, domElement) {
    updateEnableStatusService(domElement, serviceId, true);
}
function updateEnableStatusService(domElement, serviceId, enabled) {
    const suffix = enabled ? "enable" : "disable";

    if (!confirmationPrompt(suffix + " - Service", "Are you sure you want to " + suffix + " this service?"))
        return;

    const url = "/web/service/" + serviceId + "/" + suffix;
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: 'PUT',
        url: url,
        xhrFields: {
            withCredentials: true
        },
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        complete: function (xhr, textStatus) {
            if (xhr.status === 202) {
                refreshPage();
            } else {
                showAlertError("Failed to " + suffix + " service. Please consult an administrator");
            }
        }
    });
}