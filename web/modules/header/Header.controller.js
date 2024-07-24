sap.ui.controller("swv.modules.header.Header", {
    /**
     * Called when a controller is instantiated and its View controls (if
     * available) are already created. Can be used to modify the View before it
     * is displayed, to bind event handlers and do other one-time
     * initialization.
     *
     * @memberOf swv.modules.header.Header
     */
    onInit: function() {
        // set i18n model on view
        this._lastSelectedKey;

    },
    /**
     * Similar to onAfterRendering, but this hook is invoked before the
     * controller's View is re-rendered (NOT before the first rendering!
     * onInit() is used for that one!).
     *
     * @memberOf swv.modules.header.Header
     */
    onBeforeRendering: function() {

    },
    /**
     * Called when the View has been rendered (so its HTML is part of the
     * document). Post-rendering manipulations of the HTML could be done here.
     * This hook is the same one that SAPUI5 controls get after being rendered.
     *
     * @memberOf swv.modules.header.Header
     */
    onAfterRendering: function() {
        //alert("after render fired");

    },
    /**
     * Called when the Controller is destroyed. Use this one to free resources
     * and finalize activities.
     *
     * @memberOf swv.modules.header.Header
     */
    onExit: function() {

    },
    SelectTabViewAction: function() {
        var that = this;
        console.log("tab action");
        var tab = sap.ui.getCore().byId("idTabBar");
        var selectedkey = tab.getSelectedKey();
        console.log(selectedkey);
        var rpdata = sap.ui.getCore().getModel("RPConfigData");


        if (that._lastSelectedTab === selectedkey) {
            //same tab selected
            return;
        } else if (typeof rpdata !== 'undefined') {
            if (rpdata.oData.sso !== true && sap.ui.getCore().byId("X_TID").getText().length > 0) {
                //tab switched
                sap.ui.controller("swv.modules.Main").autoLogout();

            }
            that._lastSelectedTab = selectedkey;
        }
  
        if (typeof rpdata !== 'undefined') {
            if (rpdata.oData.sso && selectedkey !== "tab1" && selectedkey !== "tab5" && selectedkey !== "tab4") {
                if (sap.ui.getCore().byId("user").getUsername() === "") {
                    sap.ui.getCore().byId("idTabIcon1").removeAllContent();
                    sap.ui.getCore().byId("idTabBar").setSelectedKey("tab1");
                    sap.ui.getCore().byId("idTabBar").fireSelect({
                        key: "tab1"
                    });
                    sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "SSO UserID not found.");
                    return;
                }

            }
        }


        if (selectedkey === "tab1") {

            var rpdata = new sap.ui.getCore().getModel("RPConfigData");
            sap.ui.getCore().byId("langpopup").setVisible(true);

            var tablecontent = new sap.ui.getCore().byId("homeverticalId");
            sap.ui.getCore().byId("idTabIcon").removeAllContent();
            sap.ui.getCore().byId("idTabIcon").addContent(tablecontent);
        } else if (selectedkey === "tab2") {

            if (rpdata.oData.sso) {
                //                sap.ui.getCore().byId("sysFlag").setVisible(false);
                $.ajax({
                    beforeSend: function() {
                        sap.ui.core.BusyIndicator
                                .show(0);
                    },
                    type: "GET",
                    cache: false,
                    url: "resources/UserQA/getGID",
                    dataType: 'json',
                    async: false,
                    success: function(result) {
                        console.log("resources/UserQA/getGID");
                        if (result.success) {

                            sap.ui.getCore().byId("user").setUsername(result.GID);
                            sap.ui.getCore().byId("user").setTooltip(result.GID);
                            //sap.ui.core.BusyIndicator.show(0);
                            var rpdata = new sap.ui.getCore().getModel("RPConfigData");
                            if (rpdata.oData.multi_HOST) {
                                sap.ui.getCore().byId("F1C1E1").setVisible(false);
                                sap.ui.getCore().byId("F1C1E2").setVisible(false);
                                sap.ui.getCore().byId("F1C1E5").setVisible(false);
                                var content1 = new sap.ui.getCore().byId("F1");
                                sap.ui.getCore().byId("idTabIcon1").removeAllContent();
                                sap.ui.getCore().byId("idTabIcon1").addContent(content1);
                                sap.ui.getCore().byId("idTabBar").setSelectedKey("tab2");

                            } else {
                                sap.ui.controller("swv.modules.registration.UserRegistration").Login_butt_action();
                                //sap.ui.core.BusyIndicator.hide();
                            }
                        } else {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("user").setTooltip("");
                            sap.ui.getCore().byId("user").setUsername("");
                            sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "SSO UserID not found.");
                            sap.ui.getCore().byId("idTabBar")
                            sap.ui.getCore().byId("idTabBar").setSelectedKey("tab1");
                        }
                    },
                    error: function(results) {
                        sap.ui.core.BusyIndicator.hide();
                        console.log("resources/UserQA/getGID ajax error" + results);
                        //sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", results.message);
                    }
                });
            } else {
                sap.ui.core.BusyIndicator.show(0);
                sap.ui.getCore().byId("usertext").setValue("");
                sap.ui.getCore().byId("usertext").setValueState("None");
                sap.ui.getCore().byId("passtext").setValue("");
                sap.ui.getCore().byId("passtext").setValueState("None");
                // sap.ui.getCore().byId("sysFlag").setVisible(true);
                sap.ui.getCore().byId("langpopup").setVisible(true);
                var content1 = new sap.ui.getCore().byId("F1");
                sap.ui.getCore().byId("idTabIcon1").removeAllContent();
                sap.ui.getCore().byId("idTabIcon1").addContent(content1);
                sap.ui.core.BusyIndicator.hide();
            }
            if (!rpdata.oData.multi_HOST) {
                sap.ui.getCore().byId("F1C1E5").setVisible(true);
                sap.ui.getCore().byId("systemtext").setValue("");
                $.ajax({
                    beforeSend: function() {
                        //sap.ui.core.BusyIndicator
                        //.show(0);
                        sap.ui.getCore().byId("systemtext").setBusy(true);
                    },
                    type: "GET",
                    cache: false,
                    url: "resources/UserQA/getSingleHostSys",
                    dataType: 'json',
                    //   async: false,
                    success: function(result) {
                        console.log("resources/UserQA/getSingleHostSys");
                        if (result.success) {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("systemtext").setBusy(false);
                            sap.ui.getCore().byId("systemtext").setValue(result.System);
                            sap.ui.getCore().byId("systemtext").setEditable(false);
                            // sap.ui.getCore().byId("sysFlag").setVisible(false)
                        }
                    }
                });
            } else {
                sap.ui.getCore().byId("F1C1E4").setVisible(true);
                sap.ui.getCore().byId("systemdrop").setSelectedKey("Select");
                sap.ui.getCore().byId("systemdrop").removeStyleClass('myStateError');
                $.ajax({
                    beforeSend: function() {
                        sap.ui.core.BusyIndicator
                                .show(0);
                    },
                    type: "GET",
                    async: false,
                    cache: false,
                    url: "resources/Systems/List",
                    dataType: 'json',
                    success: function(result) {
                        console.log("resources/Systems/List");
                        if (result.total >= 1) {
                            sap.ui.core.BusyIndicator.hide();
                            //Creation of the JSON Model for the Test Daya
                            var oModelList = new sap.ui.model.json.JSONModel();
                            oModelList.setData(result);
                            sap.ui.getCore().byId("systemdrop").setModel(oModelList);
                        }
                    }
                });
            }

        } else if (selectedkey === "tab3") {
            sap.ui.getCore().byId("idTabIcon2").removeAllContent();




            var rpdata = new sap.ui.getCore().getModel("RPConfigData");

            if (rpdata.oData.sso) {
                //                sap.ui.getCore().byId("sysFlag").setVisible(false);
                $.ajax({
                    beforeSend: function() {
                        sap.ui.core.BusyIndicator
                                .show(0);
                    },
                    type: "GET",
                    cache: false,
                    url: "resources/UserQA/getGID",
                    dataType: 'json',
                    async: false,
                    success: function(result) {
                        console.log("resources/UserQA/getGID");
                        if (result.success) {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("user").setUsername(result.GID);
                            sap.ui.getCore().byId("user").setTooltip(result.GID);
                            sap.ui.getCore().byId("userIDText").setValue(result.GID);
                            if (rpdata.oData.grey_UID) {
                                sap.ui.getCore().byId("userIDText").setEditable(false);
                            } else {
                                sap.ui.getCore().byId("userIDText").setEditable(true);
                            }

                            sap.ui.getCore().byId("userIDText").setValueState("None");
                        } else {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("user").setTooltip("");
                            sap.ui.getCore().byId("user").setUsername("");
                            sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "SSO UserID not found.");
                            sap.ui.getCore().byId("idTabBar")
                            sap.ui.getCore().byId("idTabBar").setSelectedKey("tab1");

                        }
                    }
                });
            } else {
                sap.ui.core.BusyIndicator.hide();
                sap.ui.getCore().byId("userIDText").setValue("");
                sap.ui.getCore().byId("userIDText").setValueState("None");
            }

            // sap.ui.getCore().byId("sysFlag").setVisible(true);
            sap.ui.getCore().byId("langpopup").setVisible(true);
            if (!rpdata.oData.hide_NAME) {
                sap.ui.getCore().byId("reqF1C1E1").setVisible(false);
                sap.ui.getCore().byId("reqF1C1E2").setVisible(false);
            }
            if (!rpdata.oData.hide_EMAIL) {
                sap.ui.getCore().byId("reqF1C1E4").setVisible(false);
            }


            sap.ui.getCore().byId("lastNameLext").setValue("");
            sap.ui.getCore().byId("lastNameLext").setValueState("None");
            sap.ui.getCore().byId("firstNameText").setValue("");
            sap.ui.getCore().byId("firstNameText").setValueState("None");
            sap.ui.getCore().byId("EmailText").setValue("");
            sap.ui.getCore().byId("EmailText").setValueState("None");
            var content2 = new sap.ui.getCore().byId("reqF1");
            content2.addStyleClass("sapUiSizeCompact");
            sap.ui.getCore().byId("idTabIcon2").removeAllContent();
            sap.ui.getCore().byId("idTabIcon2").addContent(content2);

            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                            .show(0);
                },
                type: "GET",
                async: false,
                cache: false,
                url: "resources/Systems/List",
                dataType: 'json',
                success: function(result) {
                    console.log("resources/Systems/List");
                    if (result.total >= 1) {
                        sap.ui.core.BusyIndicator.hide();
                        //Creation of the JSON Model for the Test Daya
                        var oModelList = new sap.ui.model.json.JSONModel();
                        oModelList.setData(result);
                        sap.ui.getCore().byId("systemreqdrop").setModel(oModelList);
                    }
                }
            });
            sap.ui.getCore().byId("reqF1C1E6").setVisible(true);
            sap.ui.getCore().byId("systemreqdrop").setSelectedKey("Select");
            sap.ui.getCore().byId("systemreqdrop").removeStyleClass('myStateError');




        } else if (selectedkey === "tab6") {
            sap.ui.getCore().byId("idTabIcon2Other").removeAllContent();




            var rpdata = new sap.ui.getCore().getModel("RPConfigData");

            if (rpdata.oData.sso) {
                //                sap.ui.getCore().byId("sysFlag").setVisible(false);
                $.ajax({
                    beforeSend: function() {
                        sap.ui.core.BusyIndicator
                                .show(0);
                    },
                    type: "GET",
                    cache: false,
                    url: "resources/UserQA/getGID",
                    dataType: 'json',
                    async: false,
                    success: function(result) {
                        console.log("resources/UserQA/getGID");
                        if (result.success) {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("user").setUsername(result.GID);
                            sap.ui.getCore().byId("user").setTooltip(result.GID);
                            sap.ui.getCore().byId("userIDTextOther").setValue(result.GID);
                            if (rpdata.oData.grey_UID) {
                                sap.ui.getCore().byId("userIDTextOther").setEditable(false);
                            } else {
                                sap.ui.getCore().byId("userIDTextOther").setEditable(true);
                            }

                            sap.ui.getCore().byId("userIDTextOther").setValueState("None");
                        } else {
                            sap.ui.core.BusyIndicator.hide();
                            sap.ui.getCore().byId("user").setTooltip("");
                            sap.ui.getCore().byId("user").setUsername("");
                            sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "SSO UserID not found.");
                            sap.ui.getCore().byId("idTabBar")
                            sap.ui.getCore().byId("idTabBar").setSelectedKey("tab1");

                        }
                    }
                });
            } else {
                sap.ui.core.BusyIndicator.hide();
                sap.ui.getCore().byId("userIDTextOther").setValue("");
                sap.ui.getCore().byId("userIDTextOther").setValueState("None");
            }

            // sap.ui.getCore().byId("sysFlag").setVisible(true);
            sap.ui.getCore().byId("langpopup").setVisible(true);
            if (!rpdata.oData.hide_NAME) {
                sap.ui.getCore().byId("reqOtherF1C1E1").setVisible(false);
                sap.ui.getCore().byId("reqOtherF1C1E2").setVisible(false);
            }
            if (!rpdata.oData.hide_EMAIL) {
                sap.ui.getCore().byId("reqOtherF1C1E4").setVisible(false);
            }


            sap.ui.getCore().byId("lastNameTextOther").setValue("");
            sap.ui.getCore().byId("lastNameTextOther").setValueState("None");
            sap.ui.getCore().byId("firstNameTextOther").setValue("");
            sap.ui.getCore().byId("firstNameTextOther").setValueState("None");
            sap.ui.getCore().byId("EmailTextOther").setValue("");
            sap.ui.getCore().byId("EmailTextOther").setValueState("None");
            sap.ui.getCore().byId("userIDTextOtherApp").setValue("");
            sap.ui.getCore().byId("userIDTextOtherApp").setValueState("None");
            var content2 = new sap.ui.getCore().byId("reqOtherF1");
            content2.addStyleClass("sapUiSizeCompact");
            sap.ui.getCore().byId("idTabIcon2Other").removeAllContent();
            sap.ui.getCore().byId("idTabIcon2Other").addContent(content2);

            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                            .show(0);
                },
                type: "GET",
                async: false,
                cache: false,
                url: "resources/Systems/List",
                dataType: 'json',
                success: function(result) {
                    console.log("resources/Systems/List");
                    if (result.total >= 1) {
                        sap.ui.core.BusyIndicator.hide();
                        //Creation of the JSON Model for the Test Daya
                        var oModelList = new sap.ui.model.json.JSONModel();
                        oModelList.setData(result);
                        sap.ui.getCore().byId("systemreqdropOther").setModel(oModelList);
                    }
                }
            });
            sap.ui.getCore().byId("reqOtherF1C1E6").setVisible(true);
            sap.ui.getCore().byId("systemreqdropOther").setSelectedKey("Select");
            sap.ui.getCore().byId("systemreqdropOther").removeStyleClass('myStateError');

        }
        else if (selectedkey === "tab4") {
            sap.ui.getCore().byId("emailText").setValue("");
            sap.ui.getCore().byId("emailText").setValueState("None");
            sap.ui.getCore().byId("subjText").setValue("");
            sap.ui.getCore().byId("subjText").setValueState("None");
            sap.ui.getCore().byId("commText").setValue("");
            sap.ui.getCore().byId("commText").setValueState("None");
            var rpdata = new sap.ui.getCore().getModel("RPConfigData");
            if (rpdata.oData.multi_HOST) {
                $.ajax({
                    beforeSend: function() {
                        sap.ui.core.BusyIndicator
                                .show(0);
                    },
                    type: "GET",
                    async: false,
                    cache: false,
                    url: "resources/Systems/List",
                    dataType: 'json',
                    success: function(result) {
                        console.log("resources/Systems/List");
                        if (result.total >= 1) {
                            sap.ui.core.BusyIndicator.hide();
                            //Creation of the JSON Model for the Test Daya
                            var oModelList = new sap.ui.model.json.JSONModel();
                            oModelList.setData(result);
                            sap.ui.getCore().byId("Supsystemdrop").setModel(oModelList);
                            sap.ui.getCore().byId("Supsystemdrop").setSelectedKey("Select");
                            sap.ui.getCore().byId("Supsystemdrop").removeStyleClass('myStateError');
                        }
                    }
                });
                sap.ui.getCore().byId("SupF1C1E5").setVisible(true);
            }
            sap.ui.getCore().byId("langpopup").setVisible(true);
            var content4 = new sap.ui.getCore().byId("SupF1");
            sap.ui.getCore().byId("idTabIcon3").removeAllContent();
            sap.ui.getCore().byId("idTabIcon3").addContent(content4);

        } else if (selectedkey === "tab5") {
            var rpdata = new sap.ui.getCore().getModel("RPConfigData");
            sap.ui.getCore().byId("confText").setValue("");
            sap.ui.getCore().byId("confText").setValueState("None");
            sap.ui.getCore().byId("confpassText").setValue("");
            sap.ui.getCore().byId("confpassText").setValueState("None");
            sap.ui.getCore().byId("langpopup").setVisible(true);
            var content3 = new sap.ui.getCore().byId("confF1");
            sap.ui.getCore().byId("idTabIcon4").removeAllContent();
            sap.ui.getCore().byId("idTabIcon4").addContent(content3);
            sap.ui.getCore().byId("id_Config_jCaptcha").setBusy(true);
            sap.ui.getCore().byId("id_Config_jCaptcha").setValue("wait...");
            sap.ui.getCore().byId("id_config_image").setSrc("");
            setTimeout(function f() {//IE fix
                sap.ui.getCore().byId("id_config_image").setSrc("resources/adminCaptcha?dc=" + new Date().getTime());
            }, 200)


        }

    }

});