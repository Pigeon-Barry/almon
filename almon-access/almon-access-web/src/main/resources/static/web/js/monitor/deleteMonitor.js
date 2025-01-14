function deleteMonitor(serviceId,monitorId) {
    confirmationPrompt(monitorId + " - Delete", "Are you sure you want to delete this Monitor?", function () {
        confirmationPrompt(monitorId + " - Delete", "Are you really sure you want to delete this monitor this can not be undone?", function () {
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
                        showPopup("Delete Service", "Successfully deleted Monitor", function () {
                            redirect("/web/service/" + serviceId);
                        }, undefined);
                    } else {
                        showAlertError("Failed to delete monitor. Please consult an administrator");
                    }
                }
            });
        }, undefined);
    }, undefined);
}