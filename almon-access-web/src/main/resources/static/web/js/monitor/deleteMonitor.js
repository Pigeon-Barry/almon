function deleteMonitor(serviceId,monitorId) {
    if (!confirmationPrompt(monitorId + " - Delete", "Are you sure you want to delete this Monitor?") ||
        !confirmationPrompt(monitorId + " - Delete", "Are you really sure you want to delete this monitor this can not be undone?"))
        return;

    const url = "/web/service/" + serviceId + "/monitor/" + monitorId;
    const token = $("meta[name='_csrf']").attr("content");
    const header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        type: 'DELETE',
        url: url,
        xhrFields: {
            withCredentials: true
        },
        beforeSend: function (request) {
            request.setRequestHeader(header, token);
        },
        complete: function (xhr, textStatus) {
            if (xhr.status === 200) {
                showPopup("Delete Service", "Successfully deleted Monitor");
                redirect("/web/service/" + serviceId);
            } else {
                showAlertError("Failed to delete monitor. Please consult an administrator");
            }
        }
    });
}