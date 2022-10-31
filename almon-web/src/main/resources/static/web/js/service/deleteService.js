function deleteService(serviceId) {
    if (!confirmationPrompt(serviceId + " - Delete", "Are you sure you want to delete this service?") || !confirmationPrompt(serviceId + " - Delete", "Are you really sure you want to delete this service this can not be undone?"))
        return;

    const url = "/web/service/" + serviceId;
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
                showPopup("Delete Service", "Successfully deleted Service");
                redirect("/web/services");
            } else {
                showAlertError("Failed to delete service. Please consult an administrator");
            }
        }
    });
}