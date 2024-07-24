sap.ui.controller("swv.modules.home.Home", {
    /**
     * Called when a controller is instantiated and its View controls (if
     * available) are already created. Can be used to modify the View before it
     * is displayed, to bind event handlers and do other one-time
     * initialization.
     *
     * @memberOf swv.modules.Main
     */
    onInit: function() {

       

    },
    /**
     * Similar to onAfterRendering, but this hook is invoked before the
     * controller's View is re-rendered (NOT before the first rendering!
     * onInit() is used for that one!).
     *
     * @memberOf swv.modules.Main
     */
    onBeforeRendering: function() {

    },
    /**
     * Called when the View has been rendered (so its HTML is part of the
     * document). Post-rendering manipulations of the HTML could be done here.
     * This hook is the same one that SAPUI5 controls get after being rendered.
     *
     * @memberOf swv.modules.Main
     */
    onAfterRendering: function() {
    //alert("after render fired");

    },
    /**
     * Called when the Controller is destroyed. Use this one to free resources
     * and finalize activities.
     *
     * @memberOf swv.modules.Main
     */
    onExit: function() {

    },
    showWarning: function(title, msg) {
        jQuery.sap.require("sap.ui.commons.MessageBox");
        sap.ui.commons.MessageBox.show(
            msg,
            sap.ui.commons.MessageBox.Icon.WARNING,
            title,
            [sap.ui.commons.MessageBox.Action.OK]

            );
    },

    handleTabSelect: function(oEvent) {

        jQuery.sap.log.info("FYI: something has happened");

    }

});