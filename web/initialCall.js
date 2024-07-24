/* global sap */
try {
    sap;
} catch (err) {
    document.getElementById('content').innerHTML = "<hr><b style='color:red'>ERROR:</b> OpenUI5 v1.71.33 JavaScript library not found!<hr>";
}

sap.ui.getCore().attachInit(function () {

    jQuery.sap.log.setLevel(jQuery.sap.log.Level.ERROR);// Default is ERROR, 
    //register modules paths
    jQuery.sap.registerModulePath("swv.modules", "./modules/");        //controllers and views
    jQuery.sap.registerModulePath("swv.lib", "./js/lib/swv/");    //our own libs

    //jQuery.sap.registerModulePath("swv", "./");
    //jQuery.sap.require("swv.Component-preload");
    //load our core app lib and the global css (if cachebuster was configured, then this would be cachebuster safe!)
    jQuery.sap.require("swv.lib.app");
    app.includeStyleSheet('css/app.css');

    //now load other stuff that should be globally available
    //jQuery.sap.require("swv.lib.appConstants");
    //jQuery.sap.require("swv.lib.appUtils");

    //init our app
    app.init();
    jQuery(document).on('focus', ':input', function () {
        if (jQuery(this).is("[type=text]") || jQuery(this).is("[type=password]")) {//txt and paswd fields
            jQuery(this).attr('autocomplete', 'off');
        }
    });

    $(document).ajaxSend(function (event, jqXHR, ajaxOptions) {
        if (sap.ui.getCore().byId("X_TID") && sap.ui.getCore().byId("X_TID").getText().length > 0) {
            jqXHR.setRequestHeader('X-Enc-CSRF-Token', sap.ui.getCore().byId("X_TID").getText())
        }
    });

    $(document).ajaxError(function (event, request, settings) {
        jQuery.sap.require("sap.m.MessageBox");
        var errTitle = ""
        var errMsg = "";
        if (request.status === 200) { //OK
            return;
        }
        if (request.status === 0) {//Nework Error
            //sap.m.MessageBox.error("NetworkError: "+settings.url);
            return;
        }
        if (request.status === 400
                || request.status === 401
                || request.status === 403
                || request.status === 500) {



            errTitle = request.statusText + " !";
            if (!errTitle) {
                errTitle = "Error!";
            }
            errMsg = request.getResponseHeader("errorMessage");
            errTitle = "Error!";
            if (!errMsg) {
                errMsg = "Sorry! Something went wrong, please retry...";
            } else {

                try {
                    objError = JSON.parse(errMsg);
                    if (objError.message) {
                        errMsg = objError.message;
                    }
                } catch (e) {
                    //nothing
                }
            }

        } else if (request.responseText) {
            errTitle = "Error!";
            try {
                objError = JSON.parse(request.responseText);
                if (objError.message) {
                    errMsg = objError.message;
                } else {
                    errMsg = request.responseText;
                }
            } catch (e) {
                errMsg = request.responseText;
            }

        } else {
            errTitle = "Error!";
            errMsg = "Sorry, something went wrong! A connection or other unexpected error may have occurred.";
        }
        if (request.status === 500) {

            if (request.responseText.indexOf("NoClassDefFoundError: com/sap/conn/jco") > 0) {
                errTitle = "Error!";
                errMsg = "SAP-Java connector missing or not configured properly! \n Please add sapjco3.jar library file path to CLASSPATH environment variable of host-server.";
            }

        }

        if (!sap.ui.getCore().byId('commonErrorMsgBox')) {//No Duplicate msg if error popup msg already visible. 

            sap.m.MessageBox.error(errMsg, {
                id: "commonErrorMsgBox",
                title: errTitle,
                onClose: function (oAction) {
                    sap.ui.getCore().byId("commonErrorMsgBox").setVisible(false);
                },
                styleClass: "",
                actions: sap.m.MessageBox.Action.OK,
                emphasizedAction: null,
                initialFocus: null,
                textDirection: sap.ui.core.TextDirection.Inherit
            });
        }

        sap.ui.core.BusyIndicator.hide();//on Error
        return;
    });
});
console.log = function () {};//hide all console.log messages


