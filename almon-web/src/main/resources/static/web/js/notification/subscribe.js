function updateMonitorNotificationStatus(notificationId, monitorId, checkBoxDomElement) {
    updateNotificationStatus("/web/notification/monitor/" + monitorId, notificationId, checkBoxDomElement);
}

function updateServiceNotificationStatus(notificationId, serviceId, checkBoxDomElement) {
    updateNotificationStatus("/web/notification/service/" + serviceId, notificationId, checkBoxDomElement);
}


function updateNotificationStatus(baseUrl, notificationId, checkBoxDomElement) {
    const suffix = checkBoxDomElement.checked ? "subscribe" : "unsubscribe";
    const url = baseUrl + "/" + notificationId + "/" + suffix;

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
            if (xhr.status === 200) {
                showAlertSuccess(suffix + "d successfully");
            } else {
                if (xhr.status === 422) {
                    showAlertInfo("You are already " + suffix + "d to this notification type");
                } else if (xhr.status === 404) {
                    showAlertError(xhr.responseText);
                } else {
                    showAlertError("Failed to " + suffix + " to notification. Please consult an administrator");
                }
            }
        }
    });
}


