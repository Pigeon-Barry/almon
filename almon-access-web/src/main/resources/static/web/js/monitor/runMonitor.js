function runMonitor(serviceId, monitorId, overlayId) {
    confirmationPrompt(monitorId + " - Run", "Are you sure you want to run this Monitor?\nThis may take several minutes",
        function () {
            const url = "/web/service/" + serviceId + "/monitor/" + monitorId + "/run";
            const token = $("meta[name='_csrf']").attr("content");
            const header = $("meta[name='_csrf_header']").attr("content");

            $("#" + overlayId).show();
            $.ajax({
                type: 'POST',
                url: url,
                xhrFields: {
                    withCredentials: true
                },
                timeout: 300000,
                beforeSend: function (request) {
                    request.setRequestHeader(header, token);
                },
                complete: function (xhr, textStatus) {
                    console.log("Here");
                    $("#" + overlayId).hide();
                    if (xhr.status === 200) {
                        showPopup("Monitor Executed successfully", "Monitor Executed successfully. Alert status " + $.parseJSON(xhr.responseText).status, function () {
                            refreshPage();
                        }, undefined);
                    } else {
                        showAlertError("Failed to run monitor. Please consult an administrator");
                    }
                }
            });
        }, undefined);
}