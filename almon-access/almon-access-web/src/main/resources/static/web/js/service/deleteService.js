function deleteService(serviceId) {

    confirmationPrompt(serviceId + " - Delete", "Are you sure you want to delete this service?", function () {
        confirmationPrompt(serviceId + " - Delete", "Are you really sure you want to delete this service this can not be undone?", function () {
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
                        showPopup("Delete Service", "Successfully deleted Service", function () {
                            redirect("/web/services");
                        }, undefined);
                    } else {
                        showAlertError("Failed to delete service. Please consult an administrator");
                    }
                }
            });
        }, undefined)
    }, undefined);
}