/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery.sap.require("sap.m.IconTabBar");

sap.ui.jsview("swv.modules.header.Header", {
    /**
     * Specifies the Controller belonging to this View.
     * In the case that it is not implemented, or that "null" is returned, this View does not have a Controller.
     */
    getControllerName: function() {
        return "swv.modules.header.Header";
    },
    init: function() {

        this.createId("RPHeader");

    },
    /**
     * Is initially called once after the Controller has been instantiated. It is the place where the UI is constructed.
     * Since the Controller is given to this method, its event handlers can be attached right away.
     */
    createContent: function(oController) {

        var oIconTab = new sap.m.IconTabBar("idTabBar", {
            expandable: false,
            select: [oController.SelectTabViewAction, oController]
        });
        var itemBar1 = new sap.m.IconTabFilter({
            key: "tab1",
            id: "homeTab",
            icon: "sap-icon://home",
            text: "{i18n>IconTabBar.tab1}",
            tooltip: "{i18n>IconTabBar.tab1}",
            content: [
                new sap.ui.view({
                    id: "idTabIcon",
                    viewName: "swv.modules.home.Home",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })

        var itemBar2 = new sap.m.IconTabFilter({
            key: "tab2",
            icon: "sap-icon://customer",
            text: "{i18n>IconTabBar.tab2}",
            id: "RegisterTab",
            tooltip: "{i18n>IconTabBar.tab2}",
            content: [new sap.ui.view({
                    id: "idTabIcon1",
                    viewName: "swv.modules.registration.UserRegistration",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })
        var itemBar3 = new sap.m.IconTabFilter({
            key: "tab3",
            icon: "sap-icon://manager",
            text: "{i18n>IconTabBar.tab3}",
            id: "RequestTab",
            tooltip: "{i18n>IconTabBar.tab3}",
            content: [new sap.ui.view({
                    id: "idTabIcon2",
                    viewName: "swv.modules.request.Request",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })

        var itemBar6 = new sap.m.IconTabFilter({
            key: "tab6",
            icon: "sap-icon://manager",
            text: "{i18n>IconTabBar.tab6}",
            id: "RequestOtherTab",
            tooltip: "{i18n>IconTabBar.tab6}",
            content: [new sap.ui.view({
                    id: "idTabIcon2Other",
                    viewName: "swv.modules.requestOther.RequestOther",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })

        var itemBar4 = new sap.m.IconTabFilter({
            key: "tab4",
            icon: "sap-icon://email",
            text: "{i18n>IconTabBar.tab4}",
            id: "SupportTab",
            tooltip: "{i18n>IconTabBar.tab4}",
            content: [new sap.ui.view({
                    id: "idTabIcon3",
                    viewName: "swv.modules.support.Support",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })
        var itemBar5 = new sap.m.IconTabFilter({
            key: "tab5",
            id: "congfigTab",
            icon: "sap-icon://settings",
            text: "{i18n>IconTabBar.tab5}",
            tooltip: "{i18n>IconTabBar.tab5}",
            content: [new sap.ui.view({
                    id: "idTabIcon4",
                    viewName: "swv.modules.config.Config",
                    type: sap.ui.core.mvc.ViewType.JS
                })]
        })

        oIconTab.addItem(itemBar1);
        oIconTab.addItem(itemBar2);
        oIconTab.addItem(itemBar3);
        oIconTab.addItem(itemBar6);
        oIconTab.addItem(itemBar4);
        oIconTab.addItem(itemBar5);

        return oIconTab;


    }


});

