function disableMonitor(serviceId, monitorId, domElement) {
    updateEnableStatusMonitor(domElement, serviceId, monitorId, false);
}

function enableMonitor(serviceId, monitorId, domElement) {
    updateEnableStatusMonitor(domElement, serviceId, monitorId, true);
}

function updateEnableStatusMonitor(domElement, serviceId, monitorId, enabled) {
    const suffix = enabled ? "enable" : "disable";

    if (!confirmationPrompt(suffix + " - Service", "Are you sure you want to " + suffix + " this alert?"))
        return;

    const url = "/web/service/" + serviceId + "/monitor/" + monitorId + "/" + suffix;
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
                showAlertError("Failed to " + suffix + " alert. Please consult an administrator");
            }
        }
    });
}