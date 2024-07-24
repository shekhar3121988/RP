/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
sap.ui.controller("swv.modules.config.Config", {
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
    admin_Login_captcha_refresh : function(){
//        sap.ui.getCore().byId("id_config_image").rerender();
        //sap.ui.core.BusyIndicator.show(0);
        sap.ui.getCore().byId("id_config_image").setSrc("");
        sap.ui.getCore().byId("id_Config_jCaptcha").setValue("wait..");
        sap.ui.getCore().byId("id_Config_jCaptcha").setBusy(true);
        setTimeout(function f() {//IE fix
        sap.ui.getCore().byId("id_config_image").setSrc("resources/adminCaptcha?dc="+new Date().getTime());
        }, 200)
        
    },
    admin_Login_butt_action:function(oEvent){
        var userName=sap.ui.getCore().byId("confText").getValue();
        var password=sap.ui.getCore().byId("confpassText").getValue();
        var catptchTxt =  sap.ui.getCore().byId("id_Config_jCaptcha").getValue();
        var flag=1;
        if (userName.trim() === '') {
            sap.ui.getCore().byId("confText").setValueState("Error");
            var mess=sap.ui.getCore().byId("mesasage_Mandatory33").getText();
            sap.m.MessageToast.show(mess);
            flag=0;
        }
        else{
            sap.ui.getCore().byId("confText").setValueState("None");
        }
        if (password.trim() === '') {
            sap.ui.getCore().byId("confpassText").setValueState("Error");
            var mess=sap.ui.getCore().byId("mesasage_Mandatory33").getText();
            sap.m.MessageToast.show(mess);
            flag=0;
        }else{
            sap.ui.getCore().byId("confpassText").setValueState("None");
        }
        if (catptchTxt.trim() === '') {
            sap.ui.getCore().byId("id_Config_jCaptcha").setValueState("Error");
            var mess=sap.ui.getCore().byId("mesasage_Mandatory33").getText();
            sap.m.MessageToast.show(mess);
            flag=0;
        }else{
            sap.ui.getCore().byId("id_Config_jCaptcha").setValueState("None");
        }
        if(flag==1){
            var logindata = {
                "adminId": userName,
                "pass": sap.ui.controller("swv.modules.Main").base64String().encode(password),
                "captchaTxt": catptchTxt
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator.show(0);
                },
                cache: false,
                type: "POST",
                url: "resources/AdminLogin",
                //contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data:logindata,
                //async: false,
                success: function(result) {
                    console.log("resources/AdminLogin");
                    if(result.success){
                        //                    sap.ui.core.BusyIndicator.hide();
                        //sap.m.MessageToast.show(result.message);
                        sap.ui.getCore().byId("confText").setValue("");
                        sap.ui.getCore().byId("confpassText").setValue("");
                        sap.ui.getCore().byId("user").setUsername(userName);
                        
                         
                        if(result.xTID){
                            sap.ui.getCore().byId("X_TID").setText(result.xTID);
                        }
                        sap.ui.getCore().byId("logOff").setVisible(true);
                        
                        sap.ui.getCore().byId("homeTab").setVisible(false);
                        sap.ui.getCore().byId("RegisterTab").setVisible(false);
                        sap.ui.getCore().byId("RequestTab").setVisible(false);
                        sap.ui.getCore().byId("RequestOtherTab").setVisible(false);
                        sap.ui.getCore().byId("SupportTab").setVisible(false);
                        
                        

                        $.ajax({
                            type: "GET",
                            url: "resources/AdminList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            cache: false,
                            beforeSend: function() {
                                sap.ui.core.BusyIndicator.show(0);
                            },
                            // async: false,
                            success: function(result) {
                                console.log("resources/AdminList");
                                sap.ui.core.BusyIndicator.hide();
                                if(result.success){
                                    var oModelAdminList = new sap.ui.model.json.JSONModel();
                                    oModelAdminList.setData(result);
                                    sap.ui.getCore().byId("loginTable").setModel(oModelAdminList);
                                    sap.ui.getCore().byId("loginTable").bindRows("/data");
                                    sap.ui.getCore().byId("loginTable").setVisibleRowCount(result.total);
                                    //visibilty
                                    //admin panel button
                                    sap.ui.getCore().byId("ActiveDIRPanel").setExpanded(false);
                                    sap.ui.getCore().byId("AdmAddBtn").setVisible(false);
                                    sap.ui.getCore().byId("AdmEditBtn").setVisible(false);
                                    sap.ui.getCore().byId("AdmDeleteBtn").setVisible(false);
                                    //ad panel button
                                    sap.ui.getCore().byId("adminPanel").setExpanded(false);
                                    sap.ui.getCore().byId("ADAddBtn").setVisible(false);
                                    sap.ui.getCore().byId("ADEditBtn").setVisible(false);
                                    sap.ui.getCore().byId("ADDeleteBtn").setVisible(false);
                                    sap.ui.getCore().byId("ADTestBtn").setVisible(false);
                                    // SAp panel button hiding
                                    sap.ui.getCore().byId("sapPanel").setExpanded(false);
                                    sap.ui.getCore().byId("sapAddBtn").setVisible(false);
                                    sap.ui.getCore().byId("syncBtn").setVisible(false);
                                    sap.ui.getCore().byId("sapEditBtn").setVisible(false);
                                    sap.ui.getCore().byId("sapDeleteBtn").setVisible(false);
                                    sap.ui.getCore().byId("sapTestBtn").setVisible(false);
                                    $.ajax({
                                        type: "GET",
                                        url: "resources/Systems",
                                        contentType: "application/json; charset=utf-8",
                                        dataType: 'json',
                                        cache: false,
                                        async: false,
                                        beforeSend: function() {
                                            sap.ui.core.BusyIndicator.show(0);
                                        },
                                        success: function(result) {
                                            console.log("resources/Systems");
                                            sap.ui.core.BusyIndicator.hide();
                                            if(result.success){
                                                var oModelSAPList = new sap.ui.model.json.JSONModel();
                                                oModelSAPList.setData(result);
                                                sap.ui.getCore().byId("sapTable").setModel(oModelSAPList);
                                                sap.ui.getCore().byId("sapTable").bindRows("/data");
                                                sap.ui.getCore().byId("sapTable").setVisibleRowCount(result.total);

                                                $.ajax({
                                                    type: "GET",
                                                    cache: false,
                                                    url: "resources/Systems/getADSysList",
                                                    contentType: "application/json; charset=utf-8",
                                                    dataType: 'json',
                                                    beforeSend: function() {
                                                        sap.ui.core.BusyIndicator.show(0);
                                                    },

                                                    success: function(result) {
                                                        console.log("resources/Systems/getADSysList");
                                                        if(result.success){
                                                            var oModelSAPList = new sap.ui.model.json.JSONModel();
                                                            oModelSAPList.setData(result);
                                                            sap.ui.getCore().byId("adTable").setModel(oModelSAPList);
                                                            sap.ui.getCore().byId("adTable").bindRows("/data");
                                                            sap.ui.getCore().byId("adTable").setVisibleRowCount(result.total);
                                                            var tablecontent= new sap.ui.getCore().byId("verticalConfview");
                                                            sap.ui.getCore().byId("idTabIcon4").removeAllContent();
                                                            sap.ui.getCore().byId("idTabIcon4").addContent(tablecontent);
                                                            sap.ui.core.BusyIndicator.hide();
                                                       

                                                        }else{
                                                            sap.ui.core.BusyIndicator.hide();
                                                            sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                                        }

                                                    },
                                                    error: function(results) {
                                                        sap.ui.core.BusyIndicator.hide();
                                                        console.log("resources/Systems/getADSysList ajax error" + results);
                                                        //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                                                    }
                                                });


                                            }else{
                                                sap.ui.core.BusyIndicator.hide();
                                                sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                            }

                                        },
                                        error: function(results) {
                                            sap.ui.core.BusyIndicator.hide();
                                            console.log("resources/Systems ajax error" + results);
                                            //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                                        }
                                    });

                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("resources/AdminList error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                    }
                    else{
                        sap.ui.getCore().byId("id_config_image").setSrc("resources/adminCaptcha?dc=" + new Date().getTime());
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/AdminLogin ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });

        

       
        }else{
            return;
        }
    },
    liveChangeAction:function(){

        var userName=sap.ui.getCore().byId("confText").getValue();
        var password=sap.ui.getCore().byId("confpassText").getValue();
        if (userName.trim() === '') {
            sap.ui.getCore().byId("confText").setValueState("Error");
            var mess=sap.ui.getCore().byId("mesasage_Mandatory33").getText();
            sap.m.MessageToast.show(mess);
        }else{
            sap.ui.getCore().byId("confText").setValueState("None");
        }
        if (password.trim() === '') {
            sap.ui.getCore().byId("confpassText").setValueState("Error");
            var mess=sap.ui.getCore().byId("mesasage_Mandatory33").getText();
            sap.m.MessageToast.show(mess);
        }else{
            sap.ui.getCore().byId("confpassText").setValueState("None");
        }
    },
    ADExpandAction:function(){
        if(sap.ui.getCore().byId("ActiveDIRPanel").getExpanded()){
            sap.ui.getCore().byId("ADAddBtn").setVisible(true);
            sap.ui.getCore().byId("ADEditBtn").setVisible(true);
            sap.ui.getCore().byId("ADDeleteBtn").setVisible(true);
            sap.ui.getCore().byId("ADTestBtn").setVisible(true);
        }else{
            sap.ui.getCore().byId("ADAddBtn").setVisible(false);
            sap.ui.getCore().byId("ADEditBtn").setVisible(false);
            sap.ui.getCore().byId("ADDeleteBtn").setVisible(false);
            sap.ui.getCore().byId("ADTestBtn").setVisible(false);
        }

    },
    adminExpandAction:function(){
        if(sap.ui.getCore().byId("adminPanel").getExpanded()){
            sap.ui.getCore().byId("AdmAddBtn").setVisible(true);
            sap.ui.getCore().byId("AdmEditBtn").setVisible(true);
            sap.ui.getCore().byId("AdmDeleteBtn").setVisible(true);
        }else{
            sap.ui.getCore().byId("AdmAddBtn").setVisible(false);
            sap.ui.getCore().byId("AdmEditBtn").setVisible(false);
            sap.ui.getCore().byId("AdmDeleteBtn").setVisible(false);
        }

    },
    ADD_AD_Action:function(){
        //change state
        sap.ui.getCore().byId("addADDomaininput").setValueState("None");
        sap.ui.getCore().byId("addADIPInput").setValueState("None");
        sap.ui.getCore().byId("addADPortInput").setValueState("None");
        sap.ui.getCore().byId("addADUserIDInput").setValueState("None");
        sap.ui.getCore().byId("addADPassInput").setValueState("None");
        //clear input filed
        sap.ui.getCore().byId("addADDomaininput").setValue("");
        sap.ui.getCore().byId("addADIPInput").setValue("");
        sap.ui.getCore().byId("addADPortInput").setValue("");
        sap.ui.getCore().byId("addADUserIDInput").setValue("");
        sap.ui.getCore().byId("addADPassInput").setValue("");
        sap.ui.getCore().byId("ADAddDialog").open();
        
    },
    syncButton_Action:function(){
        
        console.log("syncButton_Action start");
        var busyDialog4 = (sap.ui.getCore().byId("confSyncBD"))
                ? sap.ui.getCore().byId("confSyncBD")
                : new sap.m.BusyDialog('confSyncBD',
                        {
                            text: 'Synchronizing Configuration',
                            title: 'Please wait',
                            customIcon:'images/synchronise_48.png',
                            customIconRotationSpeed: 1500
                        });
        console.log("syncButton_Action start");
        busyDialog4.open();
        sap.ui.getCore().byId("confSyncBD").setText("Synchronizing Configuration")
        $.ajax({
           
            type: "GET",
            cache: false,
            
            url: "resources/ConfigSync?sync=1",
            contentType: "application/json; charset=utf-8",
            dataType: 'json',
            success: function(result) {
                console.log("/ConfigSync response: "+result);
                console.log("/ConfigSync response msg: "+result.message);
                sap.ui.getCore().byId("confSyncBD").setText("Synchronizing System(s) details")
                $.ajax({
                    type: "GET",
                    cache: false,
                    url: "resources/Systems/SyncSystemDesc?sync=1",
                    contentType: "application/json; charset=utf-8",
                    dataType: 'json',
                    success: function(result) {
                        busyDialog4.close();
                        console.log("/SyncSystemDesc response: " + result);
                        console.log("/SyncSystemDesc response msg: " + result.message);
                        if (!result.success) {
                            sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", "SyncSystemDesc request unsuccessful !");
                            return;
                        }else{
                            sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", "Config. synchronized successfully.");
                        }
                    },
                    error: function(results) {
                        busyDialog4.close();
                        console.log("/SyncSystemDesc error: " + results);
                        console.log(results.message);
                        //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", "SyncSystemDesc request unsuccessful !");
                    }
                })
            },
            error: function(results) {
                console.log("/ConfigSync error: "+ results);
                console.log(results.message);
                busyDialog4.close();
                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", "ConfigSync request unsuccessful !");
            }
        });
        
        
        console.log("syncButton_Action ends");
    },
    livechange_Add_AD:function(){
        var domain= sap.ui.getCore().byId("addADDomaininput").getValue();
        var ip=sap.ui.getCore().byId("addADIPInput").getValue();
        var port=sap.ui.getCore().byId("addADPortInput").getValue();
        var user=sap.ui.getCore().byId("addADUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("addADPassInput").getValue();
        if(domain.trim()==""||domain==null){
            sap.ui.getCore().byId("addADDomaininput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
        }
        else{
            sap.ui.getCore().byId("addADDomaininput").setValueState("None");
        }
        if(ip.trim()==""||ip==null){
            sap.ui.getCore().byId("addADIPInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
        }
        else{
            sap.ui.getCore().byId("addADIPInput").setValueState("None");
        }
        if(port.trim()==""||port==null){
            sap.ui.getCore().byId("addADPortInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
        }
        else{
            sap.ui.getCore().byId("addADPortInput").setValueState("None");
        }
        if(user.trim()==""||user==null){
            sap.ui.getCore().byId("addADUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
        }
        else{
            sap.ui.getCore().byId("addADUserIDInput").setValueState("None");
        }
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("addADPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
        }
        else{
            sap.ui.getCore().byId("addADPassInput").setValueState("None");
        }
    },
    submitAdd_AD_Action:function(){
       
        var domain= sap.ui.getCore().byId("addADDomaininput").getValue();
        var ip=sap.ui.getCore().byId("addADIPInput").getValue();
        var port=sap.ui.getCore().byId("addADPortInput").getValue();
        var user=sap.ui.getCore().byId("addADUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("addADPassInput").getValue();
        var flag=true;
        if(domain.trim()==""||domain==null){
            sap.ui.getCore().byId("addADDomaininput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addADDomaininput").setValueState("None");
        }
        if(ip.trim()==""||ip==null){
            sap.ui.getCore().byId("addADIPInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addADIPInput").setValueState("None");
        }
        if(port.trim()==""||port==null){
            sap.ui.getCore().byId("addADPortInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addADPortInput").setValueState("None");
        }
        if(user.trim()==""||user==null){
            sap.ui.getCore().byId("addADUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addADUserIDInput").setValueState("None");
        }
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("addADPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addADPassInput").setValueState("None");
        }

        //        sap.ui.getCore().byId("ADAddDialog").open();
        if(flag){
            var dataAD={
                domain: domain,
                ip : ip,
                port: port,
                userid: user,
                password : sap.ui.controller("swv.modules.Main").base64String().encode(pass),
                queryAction : "N"
            }
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems/ADSystemEdit",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(dataAD),
                success: function(result) {
                    if(result.success){
                        sap.ui.getCore().byId("ADAddDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems/getADSysList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("adTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("adTable").bindRows("/data");
                                    sap.ui.getCore().byId("adTable").setVisibleRowCount(result.total);
                                   
                                    sap.ui.core.BusyIndicator.hide();


                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("Systems/ADSystemEdit ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems/ADSystemEdit ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }else{
            return ;
        }
        
    },
    modify_AD_Action:function(){
        var oTable=sap.ui.getCore().byId("adTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("adTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("adTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("adTable").getModel().getProperty(path);
            console.log(obj.domain);
            sap.ui.getCore().byId("modADDomaininput").setValueState("None");
            sap.ui.getCore().byId("modADIPInput").setValueState("None");
            sap.ui.getCore().byId("modADPortInput").setValueState("None");
            sap.ui.getCore().byId("modADUserIDInput").setValueState("None");
            sap.ui.getCore().byId("modADPassInput").setValueState("None");

            sap.ui.getCore().byId("modADDomaininput").setValue(obj.domain);
            sap.ui.getCore().byId("modADIPInput").setValue(obj.ip);
            sap.ui.getCore().byId("modADPortInput").setValue(obj.port);
            sap.ui.getCore().byId("modADUserIDInput").setValue(obj.userid);
            sap.ui.getCore().byId("modADPassInput").setValue("");
            sap.ui.getCore().byId("ADModDialog").open();
        }
    },
    Submit_modify_AD_Action:function(){
        var domain = sap.ui.getCore().byId("modADDomaininput").getValue();
        var ip = sap.ui.getCore().byId("modADIPInput").getValue();
        var port = sap.ui.getCore().byId("modADPortInput").getValue();
        var user = sap.ui.getCore().byId("modADUserIDInput").getValue();
        var pass = sap.ui.getCore().byId("modADPassInput").getValue();
        var flag=true;
        if(domain.trim()==""||domain==null){
            sap.ui.getCore().byId("modADDomaininput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modADDomaininput").setValueState("None");
        }
        if(ip.trim()==""||ip==null){
            sap.ui.getCore().byId("modADIPInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modADIPInput").setValueState("None");
        }
        if(port.trim()==""||port==null){
            sap.ui.getCore().byId("modADPortInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modADPortInput").setValueState("None");
        }
        if(user.trim()==""||user==null){
            sap.ui.getCore().byId("modADUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modADUserIDInput").setValueState("None");
        }
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("modADPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modADPassInput").setValueState("None");
        }
        if(flag){
            var dataAD={
                domain: domain,
                ip : ip,
                port: port,
                userid: user,
                password : sap.ui.controller("swv.modules.Main").base64String().encode(pass),
                queryAction : "M"
            }
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems/ADSystemEdit",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(dataAD),
                success: function(result) {
                    console.log("resources/Systems/ADSystemEdit");
                    if(result.success){
                        sap.ui.getCore().byId("ADModDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems/getADSysList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                console.log("resources/Systems/getADSysList");
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("adTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("adTable").bindRows("/data");
                                    sap.ui.getCore().byId("adTable").setVisibleRowCount(result.total);

                                    sap.ui.core.BusyIndicator.hide();


                                }
                                else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("resources/Systems/getADSysList ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems/ADSystemEdit ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
        else{
            return ;
        }
    },
    delete_AD_Action:function(){
        var oTable=sap.ui.getCore().byId("adTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("adTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("adTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("adTable").getModel().getProperty(path);
            var dataAD={
                domain: obj.domain,
                ip : "",
                port: "",
                userid: "",
                password : "",
                queryAction : "D"
            }
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems/ADSystemEdit",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(dataAD),
                success: function(result) {
                    console.log("resources/Systems/ADSystemEdit");
                    if(result.success){
                        sap.ui.getCore().byId("ADModDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems/getADSysList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                console.log("resources/Systems/getADSysList");
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("adTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("adTable").bindRows("/data");
                                    sap.ui.getCore().byId("adTable").setVisibleRowCount(result.total);

                                    sap.ui.core.BusyIndicator.hide();


                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("resources/Systems/getADSysList ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems/ADSystemEdit ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
    },
    test_AD_Action:function(){
        var oTable=sap.ui.getCore().byId("adTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }
        else{
            var contexts = sap.ui.getCore().byId("adTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("adTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("adTable").getModel().getProperty(path);
            console.log(obj.domain);
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "GET",
                cache: false,
                url: "resources/Systems/doADTest?sysName="+obj.domain,
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(result) {
                    console.log("resources/Systems/doADTest?sysName");
                    if(result.success){
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);

                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems/doADTest?sysName ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
    },
    add_admin_Action:function(){
        //change state of input
        sap.ui.getCore().byId("addAdminUserIDInput").setValueState("None");
        sap.ui.getCore().byId("addAdminPassInput").setValueState("None");
        sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("None");
        //clear input fields
        sap.ui.getCore().byId("addAdminUserIDInput").setValue("");
        sap.ui.getCore().byId("addAdminPassInput").setValue("");
        sap.ui.getCore().byId("addAdminCnfPassInput").setValue("");
        sap.ui.getCore().byId("AdminAddDialog").open();

    },
    liveChangeADDAdmin:function(){
        var id= sap.ui.getCore().byId("addAdminUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("addAdminPassInput").getValue();
        var pass1=sap.ui.getCore().byId("addAdminCnfPassInput").getValue();
        if(id.trim()==""||id==null){
            sap.ui.getCore().byId("addAdminUserIDInput").setValueState("Error");
        }
        else{
            sap.ui.getCore().byId("addAdminUserIDInput").setValueState("None");
        }
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("addAdminPassInput").setValueState("Error");
        }
        else{
            sap.ui.getCore().byId("addAdminPassInput").setValueState("None");
        }
        if(pass1.trim()==""||pass1==null){
            sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("Error");
        }
        else{
            sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("None");
        }
    },
    add_admin_SumbitAction:function(){
        var id= sap.ui.getCore().byId("addAdminUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("addAdminPassInput").getValue();
        var pass1=sap.ui.getCore().byId("addAdminCnfPassInput").getValue();
        var flag=true;
        if(id.trim()==""||id==null){
            sap.ui.getCore().byId("addAdminUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addAdminUserIDInput").setValueState("None");
        }
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("addAdminPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addAdminPassInput").setValueState("None");
        }
        if(pass1.trim()==""||pass1==null){
            sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("None");
        }
        if(pass1!==pass){
            sap.ui.getCore().byId("addAdminPassInput").setValueState("Error");
            sap.ui.getCore().byId("addAdminCnfPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password not match !");
            flag=false;
        }
        if(flag){
            if (id.length >= 5){
                flag=true;
            }
            else{
                sap.m.MessageToast.show("Admin User ID must contain at least 5 characters!");
                flag=false;
            }
        }
        if(flag){
            if (pass.match(/[a-z]/g) && pass.match(
                /[A-Z]/g) && pass.match(
                /[0-9]/g) && pass.match(
                /[^a-zA-Z\d]/g) && pass.length >= 12){
                flag=true;
            }
            else{
                sap.m.MessageToast.show("Password must contain at least 12 characters, 1 uppercase, 1 lowercase, 1 digit and 1 special character !");
                flag=false;
            }
        }
        if(flag){
            
            var result = sap.ui.controller("swv.modules.config.Config").findIntersection(id.toLowerCase(), pass.toLowerCase());
            if((result != null) && (result.length > 2)){
                sap.m.MessageToast.show("Password should not contain more than 2 characters of username!");
                flag=false;
            } else {
                flag=true;
            }
        }
        if(flag){
           var logindata={
                "adminId":id,
                "pass":pass
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/AdminLogin/ADD",
//                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data:logindata,
                success: function(result) {
                    console.log("resources/AdminLogin/ADD?Id");
                    if(result.success){
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.getCore().byId("AdminAddDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            beforeSend: function() {
                                sap.ui.core.BusyIndicator
                                .show(0);
                            },
                            type: "GET",
                            cache: false,
                            url: "resources/AdminList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                console.log("resources/AdminList");
                                if(result.success){
                                    var oModelAdminList = new sap.ui.model.json.JSONModel();
                                    oModelAdminList.setData(result);
                                    sap.ui.getCore().byId("loginTable").setModel(oModelAdminList);
                                    sap.ui.getCore().byId("loginTable").bindRows("/data");
                                    sap.ui.getCore().byId("loginTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();

                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("resources/AdminList ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });

                    }
                    else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/AdminLogin/ADD?Id ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }else{
            return;
        }
    },
    findIntersectionFromStart: function(a,b){
        for(var i=a.length;i>0;i--){
            d = a.substring(0,i);
            j = b.indexOf(d);
            if (j>=0){
                return ({
                    position:j,
                    length:i
                });
            }
        }

        return null;
    },

    findIntersection: function (a,b){
        var bestResult = null;
        for(var i=0;i<a.length-1;i++){
            var result = sap.ui.controller("swv.modules.config.Config").findIntersectionFromStart(a.substring(i),b);
            if (result){
                if (!bestResult){
                    bestResult = result;
                } else {
                    if (result.length>bestResult.length){
                        bestResult = result;
                    }
                }
            }
            if(bestResult && bestResult.length>=a.length-i)
                break;
        }
        return bestResult;
    },
    modify_admin_Action:function(){
        var oTable=sap.ui.getCore().byId("loginTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("loginTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("loginTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("loginTable").getModel().getProperty(path);
            //            console.log(obj.userID);
            //change state of input
            sap.ui.getCore().byId("modAdminUserIDInput").setValueState("None");
            sap.ui.getCore().byId("modAdminPassInput").setValueState("None");
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("None");
            //setting default value
            sap.ui.getCore().byId("modAdminUserIDInput").setValue(obj.userID);
            sap.ui.getCore().byId("modAdminPassInput").setValue("");
            sap.ui.getCore().byId("modAdminCnfPassInput").setValue("");
            sap.ui.getCore().byId("AdminModDialog").open();
           

        }
    },
    liveChangeModifyadmin:function(){
        var id= sap.ui.getCore().byId("modAdminUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("modAdminPassInput").getValue();
        var pass1=sap.ui.getCore().byId("modAdminCnfPassInput").getValue();
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("modAdminPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password is Mandatory");
        }
        else{
            sap.ui.getCore().byId("modAdminPassInput").setValueState("None");
        }
        if(pass1.trim()==""||pass1==null){
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password is Mandatory");
        }
        else{
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("None");
        }

    },
    modify_admin_SubmitAction:function(){
        
        var id= sap.ui.getCore().byId("modAdminUserIDInput").getValue();
        var pass=sap.ui.getCore().byId("modAdminPassInput").getValue();
        var pass1=sap.ui.getCore().byId("modAdminCnfPassInput").getValue();
        var flag=true;
        if(pass.trim()==""||pass==null){
            sap.ui.getCore().byId("modAdminPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password is Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modAdminPassInput").setValueState("None");
        }
        if(pass1.trim()==""||pass1==null){
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password is Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("None");
        }
        if(pass1!==pass){
            sap.ui.getCore().byId("modAdminPassInput").setValueState("Error");
            sap.ui.getCore().byId("modAdminCnfPassInput").setValueState("Error");
            sap.m.MessageToast.show("Password not match !");
            flag=false;
        }
        if(flag){
           var logindata={
                "adminId":id,
                "pass":pass
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/AdminLogin/MODIFY",
//                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data:logindata,
                success: function(result) {
                    if(result.success){
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.getCore().byId("AdminModDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            beforeSend: function() {
                                sap.ui.core.BusyIndicator
                                .show(0);
                            },
                            type: "GET",
                            cache: false,
                            url: "resources/AdminList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelAdminList = new sap.ui.model.json.JSONModel();
                                    oModelAdminList.setData(result);
                                    sap.ui.getCore().byId("loginTable").setModel(oModelAdminList);
                                    sap.ui.getCore().byId("loginTable").bindRows("/data");
                                    sap.ui.getCore().byId("loginTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();

                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("AdminList ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });

                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("/AdminLogin/MODIFY ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }else{
            return;
        }
    },
    delete_admin_Action:function(){
        var oTable=sap.ui.getCore().byId("loginTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("loginTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("loginTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("loginTable").getModel().getProperty(path);
            console.log(obj.userID);
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "GET",
                cache: false,
                url: "resources/AdminLogin/DELETE?Id="+obj.userID,
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                success: function(result) {
                    if(result.success){
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            beforeSend: function() {
                                sap.ui.core.BusyIndicator
                                .show(0);
                            },
                            type: "GET",
                            cache: false,
                            url: "resources/AdminList",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelAdminList = new sap.ui.model.json.JSONModel();
                                    oModelAdminList.setData(result);
                                    sap.ui.getCore().byId("loginTable").setModel(oModelAdminList);
                                    sap.ui.getCore().byId("loginTable").bindRows("/data");
                                    sap.ui.getCore().byId("loginTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();

                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("AdminList ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });

                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("/AdminLogin/DELETE ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
    },
    add_sap_butt_action:function(){
        //change state of input
        sap.ui.getCore().byId("addSapConnTypeSelect").setSelectedKey("NO")

        sap.ui.getCore().byId("addSapISrouterStringInput").setSelectedKey("OFF")
        sap.ui.getCore().byId("addSapRouterStringLabel").setVisible(false)
        sap.ui.getCore().byId("addSapRouterStringInput").setVisible(false)
        sap.ui.getCore().byId("addSapRouterStringInput").setValue("")
        sap.ui.getCore().byId("addSapRouterStringInput").setRequired(false)

        sap.ui.getCore().byId("addSapIPLabel").setText("IP Address")
        sap.ui.getCore().byId("addSapMsgServerPort").setVisible(false)
        sap.ui.getCore().byId("addSapMsgServerPortInput").setVisible(false)
        sap.ui.getCore().byId("addSapMsgServerPortInput").setValue("")
        sap.ui.getCore().byId("addSapR3Name").setVisible(false)
        sap.ui.getCore().byId("addSapR3NameInput").setVisible(false)
        sap.ui.getCore().byId("addSapR3NameInput").setValue("")
        sap.ui.getCore().byId("addSapLogonGroup").setVisible(false)
        sap.ui.getCore().byId("addSapLogonGroupInput").setVisible(false)
        sap.ui.getCore().byId("addSapLogonGroupInput").setValue("")
        sap.ui.getCore().byId("addSapR3NameInput").setRequired(false)
        sap.ui.getCore().byId("addSapLogonGroupInput").setRequired(false)

        sap.ui.getCore().byId("addSapSysNameinput").setValueState("None");
        sap.ui.getCore().byId("addSapRfcNameInput").setValueState("None");
        sap.ui.getCore().byId("addSapHostInput").removeStyleClass('myStateError');
        sap.ui.getCore().byId("addSapIPInput").setValueState("None");
        sap.ui.getCore().byId("addSapSysNoInput").setValueState("None");
        sap.ui.getCore().byId("addSapClientInput").setValueState("None");
        sap.ui.getCore().byId("addSapUserIDInput").setValueState("None");
        sap.ui.getCore().byId("addSapPassInput").setValueState("None");
        sap.ui.getCore().byId("addSapLangInput").setValueState("None");
        sap.ui.getCore().byId("addSapBackupInput").removeStyleClass('myStateError');
        //clear fields
        sap.ui.getCore().byId("addSapSysNameinput").setValue("");
        sap.ui.getCore().byId("addSapRfcNameInput").setValue("");
        var isHostOn = false;
        var sysArr = sap.ui.getCore().byId("sapTable").getModel().getData().data;
        for (var sysArrIndex = 0; sysArrIndex < sysArr.length; sysArrIndex++) {
            if(sysArr[sysArrIndex].host === "ON"){
                isHostOn = true;
                break;
            }
        }
        if(isHostOn){
            sap.ui.getCore().byId("addSapHostInput").setSelectedKey("OFF");
        }else{
            sap.ui.getCore().byId("addSapHostInput").setSelectedKey("ON");
        }
        //sap.ui.getCore().byId("addSapHostInput").setSelectedKey("Select");
        sap.ui.getCore().byId("addSapIPInput").setValue("");
        sap.ui.getCore().byId("addSapSysNoInput").setValue("");
        sap.ui.getCore().byId("addSapClientInput").setValue("");
        sap.ui.getCore().byId("addSapUserIDInput").setValue("");
        sap.ui.getCore().byId("addSapPassInput").setValue("");
        sap.ui.getCore().byId("addSapLangInput").setValue("EN");
        sap.ui.getCore().byId("addSapNonSapSysInput").setValue("NA");
        sap.ui.getCore().byId("addSapBackupInput").setSelectedKey("NO");

//Initially snc flag should be off and so other related flags are kept to visible off
        sap.ui.getCore().byId("addSapSncFlagInput").setSelectedKey("OFF");
        sap.ui.getCore().byId("addSapSncModeInput").setValue("");
        sap.ui.getCore().byId("addSapSncNameInput").setValue("");
        sap.ui.getCore().byId("addSapSncServiceInput").setValue("");
        sap.ui.getCore().byId("addSapSncPartnerInput").setValue("");
        sap.ui.getCore().byId("addSapSncLevelInput").setValue("");
        
        sap.ui.getCore().byId("addSapSncModeLabel").setVisible(false);
        sap.ui.getCore().byId("addSapSncModeInput").setVisible(false);
        sap.ui.getCore().byId("addSapSncNameLabel").setVisible(false);
        sap.ui.getCore().byId("addSapSncNameInput").setVisible(false);
        sap.ui.getCore().byId("addSapSncServiceLabel").setVisible(false);
        sap.ui.getCore().byId("addSapSncServiceInput").setVisible(false);
        sap.ui.getCore().byId("addSapSncPartnerLabel").setVisible(false);
        sap.ui.getCore().byId("addSapSncPartnerInput").setVisible(false);
        sap.ui.getCore().byId("addSapSncLevelLabel").setVisible(false);
        sap.ui.getCore().byId("addSapSncLevelInput").setVisible(false);
                
        sap.ui.getCore().byId("SapAddDialog").open();
    },
    add_SAP_submitAction:function(){
        
        var res=sap.ui.controller("swv.modules.config.Config").preValidation_add_Sap();
        if(res){
            var name= sap.ui.getCore().byId("addSapSysNameinput").getValue();
            var lb = sap.ui.getCore().byId("addSapConnTypeSelect").getSelectedKey();
            var r3Name =sap.ui.getCore().byId("addSapR3NameInput").getValue();
            var logonGroup =sap.ui.getCore().byId("addSapLogonGroupInput").getValue();
            var msPort =sap.ui.getCore().byId("addSapMsgServerPortInput").getValue();
            var rfc=sap.ui.getCore().byId("addSapRfcNameInput").getValue();
            var host=sap.ui.getCore().byId("addSapHostInput").getSelectedKey();
            var ip=sap.ui.getCore().byId("addSapIPInput").getValue();
            var sysno=sap.ui.getCore().byId("addSapSysNoInput").getValue();
            var client=sap.ui.getCore().byId("addSapClientInput").getValue();
            var user=sap.ui.getCore().byId("addSapUserIDInput").getValue();
            var pass=sap.ui.getCore().byId("addSapPassInput").getValue();
            var lang=sap.ui.getCore().byId("addSapLangInput").getValue();
            var backup=sap.ui.getCore().byId("addSapBackupInput").getSelectedKey();
            var ENfalg=sap.ui.getCore().byId("addSapNonSapSysInput").getValue();

            var routerFlag = sap.ui.getCore().byId("addSapISrouterStringInput").getSelectedKey()
            var routerString = sap.ui.getCore().byId("addSapRouterStringInput").getValue();
            
            var sncMode = "";
            var sncName = "";
            var sncService = "";
            var sncPartner = "";
            var sncLevel = "";
            
            var sncFlag =sap.ui.getCore().byId("addSapSncFlagInput").getSelectedKey();
                if(sncFlag=="ON"){
                    sncMode = sap.ui.getCore().byId("addSapSncModeInput").getValue();
                    sncName = sap.ui.getCore().byId("addSapSncNameInput").getValue();
                    sncService = sap.ui.getCore().byId("addSapSncServiceInput").getValue();
                    sncPartner = sap.ui.getCore().byId("addSapSncPartnerInput").getValue();
                    sncLevel = sap.ui.getCore().byId("addSapSncLevelInput").getValue();
                }
                
            if(ENfalg.trim()==""||ENfalg==null){
                ENfalg="NA";
            }
            var datasap = {
                name: name,
                user: user,
                password: sap.ui.controller("swv.modules.Main").base64String().encode(pass),
                client: client,
                ipaddress: ip,
                loadBalancing: lb,
                lbR3Name: r3Name,
                lbGroupName: logonGroup,
                lbService: msPort,
                syno: sysno,
                rfcname: rfc,
                lang: lang,
                host: host,
                backup: backup,
                queryAction: "N",
                enflag: ENfalg,
                routerFlag: routerFlag,
                routerString: routerString,
                sncMode: sncMode,
                sncName: sncName,
                sncService: sncService,
                sncPartner: sncPartner,
                sncLevel: sncLevel,
                sncFlag: sncFlag
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(datasap),
                success: function(result) {
                    if(result.success===true){
                        sap.ui.getCore().byId("SapAddDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("sapTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("sapTable").bindRows("/data");
                                    sap.ui.getCore().byId("sapTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();
                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("Systems ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                        var listData;
                        $.ajax({
           
                            type: "GET",
                            cache: false,
                            url: "resources/Systems/List",
                            dataType: 'json',
                            success: function (result) {
                                if(result.total>=1){

                                    var oModelList = new sap.ui.model.json.JSONModel();
                                    oModelList.setData(result);
                                    sap.ui.getCore().byId("systemdrop").setModel(oModelList);
                                //                                    listData=result;
                                //                                    //  console.log(listData);
                                //                                    var itemTemplate = new sap.m.StandardListItem({
                                //                                        title: "{name}",
                                //
                                //                                        active: true
                                //                                    });
                                //
                                //                                    //Creation of the JSON Model for the Test Daya
                                //                                    var oModelList = new sap.ui.model.json.JSONModel();
                                //                                    oModelList.setData(listData);
                                //                                    sap.ui.getCore().byId("SysDialog").setModel(oModelList);
                                //                                    sap.ui.getCore().byId("SysDialog").bindAggregation("items", "/data", itemTemplate);
                                //                                    sap.ui.getCore().byId("SysDialog").open();
                                }else{
                                    sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "No SAP system maintained.");

                                }
                            }
                        });




                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });

        }
    },
    preValidation_add_Sap:function(){
        var flag=true;
         
        if(sap.ui.getCore().byId("addSapSysNameinput").getValue().trim()==""||sap.ui.getCore().byId("addSapSysNameinput").getValue()==null){
            sap.ui.getCore().byId("addSapSysNameinput").setValueState("Error");
            sap.m.MessageToast.show("All fields are  Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapSysNameinput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapR3NameInput").getProperty('required')){
            if(sap.ui.getCore().byId("addSapR3NameInput").getValue().trim()==""||sap.ui.getCore().byId("addSapR3NameInput").getValue()==null){
                sap.ui.getCore().byId("addSapR3NameInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are  Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapR3NameInput").setValueState("None");
            }
        }

        if(sap.ui.getCore().byId("addSapLogonGroupInput").getProperty('required')){
            if(sap.ui.getCore().byId("addSapLogonGroupInput").getValue().trim()==""||sap.ui.getCore().byId("addSapLogonGroupInput").getValue()==null){
                sap.ui.getCore().byId("addSapLogonGroupInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapLogonGroupInput").setValueState("None");
            }
        }
        if(sap.ui.getCore().byId("addSapRouterStringInput").getProperty('required')){
            if(sap.ui.getCore().byId("addSapRouterStringInput").getValue().trim()==""||sap.ui.getCore().byId("addSapRouterStringInput").getValue()==null){
                sap.ui.getCore().byId("addSapRouterStringInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are  Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapRouterStringInput").setValueState("None");
            }
        }
        if(sap.ui.getCore().byId("addSapRfcNameInput").getValue().trim()==""||sap.ui.getCore().byId("addSapRfcNameInput").getValue()==null){
            sap.ui.getCore().byId("addSapRfcNameInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapRfcNameInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapHostInput").getSelectedKey()=="Select")
        {
            sap.ui.getCore().byId("addSapHostInput").addStyleClass('myStateError');
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapHostInput").removeStyleClass('myStateError');
        }
        if(sap.ui.getCore().byId("addSapIPInput").getValue().trim()==""||sap.ui.getCore().byId("addSapIPInput").getValue()==null){
            sap.ui.getCore().byId("addSapIPInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapIPInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapSysNoInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSysNoInput").getValue()==null){
            sap.ui.getCore().byId("addSapSysNoInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapSysNoInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapClientInput").getValue().trim()==""||sap.ui.getCore().byId("addSapClientInput").getValue()==null){
            sap.ui.getCore().byId("addSapClientInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapClientInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapUserIDInput").getValue().trim()==""||sap.ui.getCore().byId("addSapUserIDInput").getValue()==null){
            sap.ui.getCore().byId("addSapUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapUserIDInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapPassInput").getValue().trim()==""||sap.ui.getCore().byId("addSapPassInput").getValue()==null){
            sap.ui.getCore().byId("addSapPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapPassInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapLangInput").getValue().trim()==""||sap.ui.getCore().byId("addSapLangInput").getValue()==null){
            sap.ui.getCore().byId("addSapLangInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapLangInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("addSapBackupInput").getSelectedKey()=="Select")
        {
            sap.ui.getCore().byId("addSapBackupInput").addStyleClass('myStateError');
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("addSapBackupInput").removeStyleClass('myStateError');
        }
        
             
        if(sap.ui.getCore().byId("addSapSncFlagInput").getSelectedKey() == "ON"){
            if(sap.ui.getCore().byId("addSapSncModeInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSncModeInput").getValue()==null){
                sap.ui.getCore().byId("addSapSncModeInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapSncModeInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("addSapSncNameInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSncNameInput").getValue()==null){
                sap.ui.getCore().byId("addSapSncNameInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapSncNameInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("addSapSncServiceInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSncServiceInput").getValue()==null){
                sap.ui.getCore().byId("addSapSncServiceInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapSncServiceInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("addSapSncPartnerInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSncPartnerInput").getValue()==null){
                sap.ui.getCore().byId("addSapSncPartnerInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapSncPartnerInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("addSapSncLevelInput").getValue().trim()==""||sap.ui.getCore().byId("addSapSncLevelInput").getValue()==null){
                sap.ui.getCore().byId("addSapSncLevelInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("addSapSncLevelInput").setValueState("None");
            }
        }
        
        if(flag){
            return true;
        }else{
            return false;
        }
    },
    delete_SAP_Action:function(){
        var oTable=sap.ui.getCore().byId("sapTable");
        
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else if(oTable.getModel().getData().data.length===1){
            sap.m.MessageToast.show(
                "Deletion of the only single maintained record not allowed! First add another system and then retry to delete it.", {
                    width: "100rem"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("sapTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("sapTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("sapTable").getModel().getProperty(path);
            console.log(obj.name);
            console.log(obj.sncName);
            console.log(obj.loadBalancing);
            var datasap={
                name:obj.name,
                backup:obj.backup,
                password:"",
                queryAction:"D"
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(datasap),
                success: function(result) {
                    if(result.success===true){
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("sapTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("sapTable").bindRows("/data");
                                    sap.ui.getCore().byId("sapTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();
                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("Systems ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                        var listData;
                        $.ajax({

                            type: "GET",
                            cache: false,
                            url: "resources/Systems/List",
                            dataType: 'json',
                            success: function (result) {
                                if(result.total>=1){

                                    var oModelList = new sap.ui.model.json.JSONModel();
                                    oModelList.setData(result);
                                    sap.ui.getCore().byId("systemdrop").setModel(oModelList);
                                //
                                //                                      listData=result;
                                //                                    //  console.log(listData);
                                //                                    var itemTemplate = new sap.m.StandardListItem({
                                //                                        title: "{name}",
                                //
                                //                                        active: true
                                //                                    });
                                //
                                //                                    //Creation of the JSON Model for the Test Daya
                                //                                    var oModelList = new sap.ui.model.json.JSONModel();
                                //                                    oModelList.setData(listData);
                                //                                    sap.ui.getCore().byId("SysDialog").setModel(oModelList);
                                //                                    sap.ui.getCore().byId("SysDialog").bindAggregation("items", "/data", itemTemplate);
                                //                                    sap.ui.getCore().byId("SysDialog").open();
                                }
                                else{
                                    sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "No SAP system maintained.");

                                }
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
    },
    modify_SAP_Action:function(){
        var oTable=sap.ui.getCore().byId("sapTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }
        else{
            var contexts = sap.ui.getCore().byId("sapTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("sapTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("sapTable").getModel().getProperty(path);
            console.log(obj.name);
            console.log(obj.sncName);
            console.log(obj.loadBalancing);
            console.log(obj.routerFlag);

            if(obj.routerFlag === "ON"){
                sap.ui.getCore().byId("modSapISrouterStringInput").setSelectedKey("ON")
                sap.ui.getCore().byId("modSapRouterStringLabel").setVisible(true)
                sap.ui.getCore().byId("modSapRouterStringInput").setValue(obj.routerString)
                sap.ui.getCore().byId("modSapRouterStringInput").setVisible(true)
                sap.ui.getCore().byId("modSapRouterStringInput").setRequired(true)
            }else{
                sap.ui.getCore().byId("modSapISrouterStringInput").setSelectedKey("OFF")
                sap.ui.getCore().byId("modSapRouterStringLabel").setVisible(false)
                sap.ui.getCore().byId("modSapRouterStringInput").setValue("")
                sap.ui.getCore().byId("modSapRouterStringInput").setVisible(false)
                sap.ui.getCore().byId("modSapRouterStringInput").setRequired(false)
            }

            if(obj.loadBalancing === "YES"){

                sap.ui.getCore().byId("modSapConnTypeSelect").setSelectedKey("YES")
                
                sap.ui.getCore().byId("modSapIPLabel").setText("Host Message Server")
                
                sap.ui.getCore().byId("modSapMsgServerPort").setVisible(true)
                sap.ui.getCore().byId("modSapMsgServerPortInput").setVisible(true)
                sap.ui.getCore().byId("modSapMsgServerPortInput").setValue(obj.lbService);

                sap.ui.getCore().byId("modSapR3Name").setVisible(true)
                sap.ui.getCore().byId("modSapR3NameInput").setVisible(true)
                sap.ui.getCore().byId("modSapR3NameInput").setValue(obj.lbR3Name);


                sap.ui.getCore().byId("modSapLogonGroup").setVisible(true)
                sap.ui.getCore().byId("modSapLogonGroupInput").setVisible(true)
                sap.ui.getCore().byId("modSapLogonGroupInput").setValue(obj.lbGroupName);

                
                sap.ui.getCore().byId("modSapR3NameInput").setRequired(true)
                sap.ui.getCore().byId("modSapLogonGroupInput").setRequired(true)
                
                
            }else{
                sap.ui.getCore().byId("modSapConnTypeSelect").setSelectedKey("NO")

                sap.ui.getCore().byId("modSapIPLabel").setText("IP Address")
                sap.ui.getCore().byId("modSapMsgServerPort").setVisible(false)
                sap.ui.getCore().byId("modSapMsgServerPortInput").setVisible(false)
                sap.ui.getCore().byId("modSapMsgServerPortInput").setValue("")
                sap.ui.getCore().byId("modSapR3Name").setVisible(false)
                sap.ui.getCore().byId("modSapR3NameInput").setVisible(false)
                sap.ui.getCore().byId("modSapR3NameInput").setValue("")
                sap.ui.getCore().byId("modSapLogonGroup").setVisible(false)
                sap.ui.getCore().byId("modSapLogonGroupInput").setVisible(false)
                sap.ui.getCore().byId("modSapLogonGroupInput").setValue("")
                sap.ui.getCore().byId("modSapR3NameInput").setRequired(false)
                sap.ui.getCore().byId("modSapLogonGroupInput").setRequired(false)
            
            }
                
            
            sap.ui.getCore().byId("modSapSysNameinput").setValue(obj.name);
            sap.ui.getCore().byId("modSapRfcNameInput").setValue(obj.rfcname);
            sap.ui.getCore().byId("modSapHostInput").setSelectedKey(obj.host);
            sap.ui.getCore().byId("modSapIPInput").setValue(obj.ipaddress);
            sap.ui.getCore().byId("modSapSysNoInput").setValue(obj.syno);
            sap.ui.getCore().byId("modSapClientInput").setValue(obj.client);
            sap.ui.getCore().byId("modSapUserIDInput").setValue(obj.user);
            sap.ui.getCore().byId("modSapLangInput").setValue(obj.lang);
            sap.ui.getCore().byId("modSapBackupInput").setSelectedKey(obj.backup);
            sap.ui.getCore().byId("modSapNonSapSysInput").setValue(obj.enflag);
            sap.ui.getCore().byId("modSapPassInput").setValue("");
            
            sap.ui.getCore().byId("modSapSncFlagInput").setSelectedKey(obj.sncFlag);
            sap.ui.getCore().byId("modSapSncModeInput").setValue(obj.sncMode);
            sap.ui.getCore().byId("modSapSncNameInput").setValue(obj.sncName);
            sap.ui.getCore().byId("modSapSncServiceInput").setValue(obj.sncService);
            sap.ui.getCore().byId("modSapSncPartnerInput").setValue(obj.sncPartner);
            sap.ui.getCore().byId("modSapSncLevelInput").setValue(obj.sncLevel);
            
            if(obj.sncFlag=="ON"){
                
                sap.ui.getCore().byId("modSapSncModeLabel").setVisible(true);
                sap.ui.getCore().byId("modSapSncModeInput").setVisible(true);
                sap.ui.getCore().byId("modSapSncNameLabel").setVisible(true);
                sap.ui.getCore().byId("modSapSncNameInput").setVisible(true);
                sap.ui.getCore().byId("modSapSncServiceLabel").setVisible(true);
                sap.ui.getCore().byId("modSapSncServiceInput").setVisible(true);
                sap.ui.getCore().byId("modSapSncPartnerLabel").setVisible(true);
                sap.ui.getCore().byId("modSapSncPartnerInput").setVisible(true);
                sap.ui.getCore().byId("modSapSncLevelLabel").setVisible(true);
                sap.ui.getCore().byId("modSapSncLevelInput").setVisible(true);
            }else{
                sap.ui.getCore().byId("modSapSncModeLabel").setVisible(false);
                sap.ui.getCore().byId("modSapSncModeInput").setVisible(false);
                sap.ui.getCore().byId("modSapSncNameLabel").setVisible(false);
                sap.ui.getCore().byId("modSapSncNameInput").setVisible(false);
                sap.ui.getCore().byId("modSapSncServiceLabel").setVisible(false);
                sap.ui.getCore().byId("modSapSncServiceInput").setVisible(false);
                sap.ui.getCore().byId("modSapSncPartnerLabel").setVisible(false);
                sap.ui.getCore().byId("modSapSncPartnerInput").setVisible(false);
                sap.ui.getCore().byId("modSapSncLevelLabel").setVisible(false);
                sap.ui.getCore().byId("modSapSncLevelInput").setVisible(false);
            }
            sap.ui.getCore().byId("SapModDialog").open();
        }
    },
     modify_Sap_SncString:function(){

        var type=sap.ui.getCore().byId("modSapSncFlagInput").getSelectedKey();
        if(type=="ON"){
            sap.ui.getCore().byId("modSapSncModeLabel").setVisible(true);
            sap.ui.getCore().byId("modSapSncModeInput").setVisible(true);
            sap.ui.getCore().byId("modSapSncNameLabel").setVisible(true);
            sap.ui.getCore().byId("modSapSncNameInput").setVisible(true);
            sap.ui.getCore().byId("modSapSncServiceLabel").setVisible(true);
            sap.ui.getCore().byId("modSapSncServiceInput").setVisible(true);
            sap.ui.getCore().byId("modSapSncPartnerLabel").setVisible(true);
            sap.ui.getCore().byId("modSapSncPartnerInput").setVisible(true);
            sap.ui.getCore().byId("modSapSncLevelLabel").setVisible(true);
            sap.ui.getCore().byId("modSapSncLevelInput").setVisible(true);
        }else{
            sap.ui.getCore().byId("modSapSncModeLabel").setVisible(false);
            sap.ui.getCore().byId("modSapSncModeInput").setVisible(false);
            sap.ui.getCore().byId("modSapSncNameLabel").setVisible(false);
            sap.ui.getCore().byId("modSapSncNameInput").setVisible(false);
            sap.ui.getCore().byId("modSapSncServiceLabel").setVisible(false);
            sap.ui.getCore().byId("modSapSncServiceInput").setVisible(false);
            sap.ui.getCore().byId("modSapSncPartnerLabel").setVisible(false);
            sap.ui.getCore().byId("modSapSncPartnerInput").setVisible(false);
            sap.ui.getCore().byId("modSapSncLevelLabel").setVisible(false);
            sap.ui.getCore().byId("modSapSncLevelInput").setVisible(false);
        }

    },
    add_Sap_SncString:function(){

        var type=sap.ui.getCore().byId("addSapSncFlagInput").getSelectedKey();
        if(type=="ON"){
            sap.ui.getCore().byId("addSapSncModeLabel").setVisible(true);
            sap.ui.getCore().byId("addSapSncModeInput").setVisible(true);
            sap.ui.getCore().byId("addSapSncNameLabel").setVisible(true);
            sap.ui.getCore().byId("addSapSncNameInput").setVisible(true);
            sap.ui.getCore().byId("addSapSncServiceLabel").setVisible(true);
            sap.ui.getCore().byId("addSapSncServiceInput").setVisible(true);
            sap.ui.getCore().byId("addSapSncPartnerLabel").setVisible(true);
            sap.ui.getCore().byId("addSapSncPartnerInput").setVisible(true);
            sap.ui.getCore().byId("addSapSncLevelLabel").setVisible(true);
            sap.ui.getCore().byId("addSapSncLevelInput").setVisible(true);
        }else{
            sap.ui.getCore().byId("addSapSncModeLabel").setVisible(false);
            sap.ui.getCore().byId("addSapSncModeInput").setVisible(false);
            sap.ui.getCore().byId("addSapSncNameLabel").setVisible(false);
            sap.ui.getCore().byId("addSapSncNameInput").setVisible(false);
            sap.ui.getCore().byId("addSapSncServiceLabel").setVisible(false);
            sap.ui.getCore().byId("addSapSncServiceInput").setVisible(false);
            sap.ui.getCore().byId("addSapSncPartnerLabel").setVisible(false);
            sap.ui.getCore().byId("addSapSncPartnerInput").setVisible(false);
            sap.ui.getCore().byId("addSapSncLevelLabel").setVisible(false);
            sap.ui.getCore().byId("addSapSncLevelInput").setVisible(false);
        }

    },
    Submit_modify_SAP_Action:function(){
        var res=sap.ui.controller("swv.modules.config.Config").preValidation_mod_sap();
        if(res){
            console.log(res);
            var name= sap.ui.getCore().byId("modSapSysNameinput").getValue();
            var lb = sap.ui.getCore().byId("modSapConnTypeSelect").getSelectedKey();
            var r3Name =sap.ui.getCore().byId("modSapR3NameInput").getValue();
            var logonGroup =sap.ui.getCore().byId("modSapLogonGroupInput").getValue();
            var msPort =sap.ui.getCore().byId("modSapMsgServerPortInput").getValue();

            var routerFlag = sap.ui.getCore().byId("modSapISrouterStringInput").getSelectedKey()
            var routerString = sap.ui.getCore().byId("modSapRouterStringInput").getValue();
            
            var rfc=sap.ui.getCore().byId("modSapRfcNameInput").getValue();
            var host=sap.ui.getCore().byId("modSapHostInput").getSelectedKey();
            var ip=sap.ui.getCore().byId("modSapIPInput").getValue();
            var sysno=sap.ui.getCore().byId("modSapSysNoInput").getValue();
            var client=sap.ui.getCore().byId("modSapClientInput").getValue();
            var user=sap.ui.getCore().byId("modSapUserIDInput").getValue();
            var pass=sap.ui.getCore().byId("modSapPassInput").getValue();
            var lang=sap.ui.getCore().byId("modSapLangInput").getValue();
            var backup=sap.ui.getCore().byId("modSapBackupInput").getSelectedKey();
            var ENfalg=sap.ui.getCore().byId("modSapNonSapSysInput").getValue();
            
            var sncMode = "";
            var sncName = "";
            var sncService = "";
            var sncPartner = "";
            var sncLevel = "";
            var lbR3Name = "";
            var lbGroupName = "";
            var lbService = "";


            var sncFlag =sap.ui.getCore().byId("modSapSncFlagInput").getSelectedKey();
            if(sncFlag=="ON"){
                sncMode = sap.ui.getCore().byId("modSapSncModeInput").getValue();
                sncName = sap.ui.getCore().byId("modSapSncNameInput").getValue();
                sncService = sap.ui.getCore().byId("modSapSncServiceInput").getValue();
                sncPartner = sap.ui.getCore().byId("modSapSncPartnerInput").getValue();
                sncLevel = sap.ui.getCore().byId("modSapSncLevelInput").getValue();
            }
            
            if(ENfalg.trim()==""||ENfalg==null){
                ENfalg="NA";
            }
            var datasap = {
                name: name,
                user: user,
                routerFlag: routerFlag,
                routerString: routerString,
                loadBalancing: lb,
                lbR3Name: r3Name,
                lbGroupName: logonGroup,
                lbService: msPort,
                password: sap.ui.controller("swv.modules.Main").base64String().encode(pass),
                client: client,
                ipaddress: ip,
                syno: sysno,
                rfcname: rfc,
                lang: lang,
                host: host,
                backup: backup,
                queryAction: "M",
                enflag: ENfalg,
                sncMode: sncMode,
                sncName: sncName,
                sncService: sncService,
                sncPartner: sncPartner,
                sncLevel: sncLevel,
                sncFlag: sncFlag
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "POST",
                cache: false,
                url: "resources/Systems",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : JSON.stringify(datasap),
                success: function(result) {
                    if(result.success===true){
                        sap.ui.getCore().byId("SapModDialog").close();
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("sapTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("sapTable").bindRows("/data");
                                    sap.ui.getCore().byId("sapTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();
                                }else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }

                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("Systems ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                        var listData;
                        $.ajax({

                            type: "GET",
                            cache: false,
                            url: "resources/Systems/List",
                            dataType: 'json',
                            success: function (result) {
                                if(result.total>=1){
                                    var oModelList = new sap.ui.model.json.JSONModel();
                                    oModelList.setData(result);
                                    sap.ui.getCore().byId("systemdrop").setModel(oModelList);


                                //                                    listData=result;
                                //                                    //  console.log(listData);
                                //                                    var itemTemplate = new sap.m.StandardListItem({
                                //                                        title: "{name}",
                                //
                                //                                        active: true
                                //                                    });
                                //
                                //                                    //Creation of the JSON Model for the Test Daya
                                //                                    var oModelList = new sap.ui.model.json.JSONModel();
                                //                                    oModelList.setData(listData);
                                //                                    sap.ui.getCore().byId("SysDialog").setModel(oModelList);
                                //                                    sap.ui.getCore().byId("SysDialog").bindAggregation("items", "/data", itemTemplate);
                                //                                    sap.ui.getCore().byId("SysDialog").open();
                                }else{
                                    sap.ui.controller("swv.modules.Main").showError("{i18n>message.Error}", "No SAP system maintained.");

                                }
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }

                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems ajax error" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });
        }
    },
    preValidation_mod_sap:function(){

        var flag=true;

        if(sap.ui.getCore().byId("modSapSysNameinput").getValue().trim()==""||sap.ui.getCore().byId("modSapSysNameinput").getValue()==null){
            sap.ui.getCore().byId("modSapSysNameinput").setValueState("Error");
            sap.m.MessageToast.show("All fields are  Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapSysNameinput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapR3NameInput").getProperty('required')){
            if(sap.ui.getCore().byId("modSapR3NameInput").getValue().trim()==""||sap.ui.getCore().byId("modSapR3NameInput").getValue()==null){
                sap.ui.getCore().byId("modSapR3NameInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are  Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapR3NameInput").setValueState("None");
            }
        }

        if(sap.ui.getCore().byId("modSapLogonGroupInput").getProperty('required')){
            if(sap.ui.getCore().byId("modSapLogonGroupInput").getValue().trim()==""||sap.ui.getCore().byId("modSapLogonGroupInput").getValue()==null){
                sap.ui.getCore().byId("modSapLogonGroupInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapLogonGroupInput").setValueState("None");
            }
        }
        if(sap.ui.getCore().byId("modSapRouterStringInput").getProperty('required')){
            if(sap.ui.getCore().byId("modSapRouterStringInput").getValue().trim()==""||sap.ui.getCore().byId("modSapRouterStringInput").getValue()==null){
                sap.ui.getCore().byId("modSapRouterStringInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are  Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapRouterStringInput").setValueState("None");
            }
        }
        if(sap.ui.getCore().byId("modSapHostInput").getSelectedKey()=="Select")
        {
            sap.ui.getCore().byId("modSapHostInput").addStyleClass('myStateError');
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapHostInput").removeStyleClass('myStateError');
        }
        if(sap.ui.getCore().byId("modSapIPInput").getValue().trim()==""||sap.ui.getCore().byId("modSapIPInput").getValue()==null){
            sap.ui.getCore().byId("modSapIPInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapIPInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapSysNoInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSysNoInput").getValue()==null){
            sap.ui.getCore().byId("modSapSysNoInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapSysNoInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapClientInput").getValue().trim()==""||sap.ui.getCore().byId("modSapClientInput").getValue()==null){
            sap.ui.getCore().byId("modSapClientInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapClientInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapUserIDInput").getValue().trim()==""||sap.ui.getCore().byId("modSapUserIDInput").getValue()==null){
            sap.ui.getCore().byId("modSapUserIDInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapUserIDInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapPassInput").getValue().trim()==""||sap.ui.getCore().byId("modSapPassInput").getValue()==null){
            sap.ui.getCore().byId("modSapPassInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapPassInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapLangInput").getValue().trim()==""||sap.ui.getCore().byId("modSapLangInput").getValue()==null){
            sap.ui.getCore().byId("modSapLangInput").setValueState("Error");
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapLangInput").setValueState("None");
        }
        if(sap.ui.getCore().byId("modSapBackupInput").getSelectedKey()=="Select")
        {
            sap.ui.getCore().byId("modSapBackupInput").addStyleClass('myStateError');
            sap.m.MessageToast.show("All fields are Mandatory");
            flag=false;
        }
        else{
            sap.ui.getCore().byId("modSapBackupInput").removeStyleClass('myStateError');
        }
        
        if(sap.ui.getCore().byId("modSapSncFlagInput").getSelectedKey() == "ON"){
            if(sap.ui.getCore().byId("modSapSncModeInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSncModeInput").getValue()==null){
                sap.ui.getCore().byId("modSapSncModeInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapSncModeInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("modSapSncNameInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSncNameInput").getValue()==null){
                sap.ui.getCore().byId("modSapSncNameInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapSncNameInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("modSapSncServiceInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSncServiceInput").getValue()==null){
                sap.ui.getCore().byId("modSapSncServiceInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapSncServiceInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("modSapSncPartnerInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSncPartnerInput").getValue()==null){
                sap.ui.getCore().byId("modSapSncPartnerInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapSncPartnerInput").setValueState("None");
            }

            if(sap.ui.getCore().byId("modSapSncLevelInput").getValue().trim()==""||sap.ui.getCore().byId("modSapSncLevelInput").getValue()==null){
                sap.ui.getCore().byId("modSapSncLevelInput").setValueState("Error");
                sap.m.MessageToast.show("All fields are Mandatory");
                flag=false;
            }
            else{
                sap.ui.getCore().byId("modSapSncLevelInput").setValueState("None");
            }
        }
        
        if(flag){
            return true;
        }else{
            return false;
        }
    },
    test_SAP_Action:function(){
        var oTable=sap.ui.getCore().byId("sapTable");
        var aSelected=oTable.getSelectedIndices();
        if (aSelected.length == 0) {
            sap.m.MessageToast.show(
                "Please select atleast one row", {
                    width: "25em"

                });
            return;
        }else{
            var contexts = sap.ui.getCore().byId("sapTable").getSelectedIndex();
            // get index of selected row
            var items = sap.ui.getCore().byId("sapTable").getContextByIndex(contexts);
            var path = items.sPath;
            var obj = sap.ui.getCore().byId("sapTable").getModel().getProperty(path);
            console.log(obj.name);
            console.log(obj.backup);
            // console.log(obj.loadBalancing);

               

            var datasap={
                name:obj.name,
                backup:obj.backup
            };
            $.ajax({
                beforeSend: function() {
                    sap.ui.core.BusyIndicator
                    .show(0);
                },
                type: "GET",
                cache: false,
                url: "resources/Systems/doTest",
                contentType: "application/json; charset=utf-8",
                dataType: 'json',
                data : datasap,
                success: function(result) {
                    if(result.success===true){
                        sap.ui.controller("swv.modules.config.Config").showSuccess("{i18n>message.Success}", result.message);
                        $.ajax({
                            type: "GET",
                            cache: false,
                            url: "resources/Systems",
                            contentType: "application/json; charset=utf-8",
                            dataType: 'json',
                            success: function(result) {
                                if(result.success){
                                    var oModelSAPList = new sap.ui.model.json.JSONModel();
                                    oModelSAPList.setData(result);
                                    sap.ui.getCore().byId("sapTable").setModel(oModelSAPList);
                                    sap.ui.getCore().byId("sapTable").bindRows("/data");
                                    sap.ui.getCore().byId("sapTable").setVisibleRowCount(result.total);
                                    sap.ui.core.BusyIndicator.hide();
                                }
                                else{
                                    sap.ui.core.BusyIndicator.hide();
                                    sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                                }
        
                            },
                            error: function(results) {
                                sap.ui.core.BusyIndicator.hide();
                                console.log("Systems ajax error" + results);
                                //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                            }
                        });
                    }else{
                        sap.ui.core.BusyIndicator.hide();
                        sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", result.message);
                    }
        
                },
                error: function(results) {
                    sap.ui.core.BusyIndicator.hide();
                    console.log("resources/Systems/doTest" + results);
                    //sap.ui.controller("swv.modules.config.Config").showError("{i18n>message.Error}", results.message);
                }
            });


        }
    },
    sapExpandAction:function(){
        if(sap.ui.getCore().byId("sapPanel").getExpanded()){
            sap.ui.getCore().byId("sapAddBtn").setVisible(true);
            sap.ui.getCore().byId("syncBtn").setVisible(true);
            sap.ui.getCore().byId("sapEditBtn").setVisible(true);
            sap.ui.getCore().byId("sapDeleteBtn").setVisible(true);
            sap.ui.getCore().byId("sapTestBtn").setVisible(true);
        }else{
            sap.ui.getCore().byId("sapAddBtn").setVisible(false);
            sap.ui.getCore().byId("syncBtn").setVisible(false);
            sap.ui.getCore().byId("sapEditBtn").setVisible(false);
            sap.ui.getCore().byId("sapDeleteBtn").setVisible(false);
            sap.ui.getCore().byId("sapTestBtn").setVisible(false);
        }

    },
    showError: function(title, msg) {
        jQuery.sap.require("sap.m.MessageBox");
        sap.m.MessageBox.error(msg, {
            title: title,
            onClose: null    ,

            initialFocus: null ,
            textDirection: sap.ui.core.TextDirection.Inherit
        });
    },
    showSuccess: function(title, msg) {
        jQuery.sap.require("sap.m.MessageBox");
        sap.m.MessageBox.success(msg, {
            title: title,
            onClose: null,
            initialFocus: null ,
            textDirection: sap.ui.core.TextDirection.Inherit
        });
    },
    modConnectionTypeChange: function(oControlEvent) {
        var selItem = oControlEvent.getParameter("selectedItem").getProperty('key')
        if(selItem === "YES"){
            sap.ui.getCore().byId("modSapIPLabel").setText("Host Message Server")
            sap.ui.getCore().byId("modSapMsgServerPort").setVisible(true)
            sap.ui.getCore().byId("modSapMsgServerPortInput").setVisible(true)
            sap.ui.getCore().byId("modSapR3Name").setVisible(true)
            sap.ui.getCore().byId("modSapR3NameInput").setVisible(true)
            sap.ui.getCore().byId("modSapLogonGroup").setVisible(true)
            sap.ui.getCore().byId("modSapLogonGroupInput").setVisible(true)
            sap.ui.getCore().byId("modSapR3NameInput").setRequired(true)
            sap.ui.getCore().byId("modSapLogonGroupInput").setRequired(true)
            
        }else{
            sap.ui.getCore().byId("modSapIPLabel").setText("IP Address")
            sap.ui.getCore().byId("modSapMsgServerPort").setVisible(false)
            sap.ui.getCore().byId("modSapMsgServerPortInput").setVisible(false)
            sap.ui.getCore().byId("modSapMsgServerPortInput").setValue("")
            sap.ui.getCore().byId("modSapR3Name").setVisible(false)
            sap.ui.getCore().byId("modSapR3NameInput").setVisible(false)
            sap.ui.getCore().byId("modSapR3NameInput").setValue("")
            sap.ui.getCore().byId("modSapLogonGroup").setVisible(false)
            sap.ui.getCore().byId("modSapLogonGroupInput").setVisible(false)
            sap.ui.getCore().byId("modSapLogonGroupInput").setValue("")
            sap.ui.getCore().byId("modSapR3NameInput").setRequired(false)
            sap.ui.getCore().byId("modSapLogonGroupInput").setRequired(false)
        }
    },
    addConnectionTypeChange: function(oControlEvent) {
        var selItem = oControlEvent.getParameter("selectedItem").getProperty('key')
        if(selItem === "YES"){
            sap.ui.getCore().byId("addSapIPLabel").setText("Host Message Server")
            sap.ui.getCore().byId("addSapMsgServerPort").setVisible(true)
            sap.ui.getCore().byId("addSapMsgServerPortInput").setVisible(true)
            sap.ui.getCore().byId("addSapR3Name").setVisible(true)
            sap.ui.getCore().byId("addSapR3NameInput").setVisible(true)
            sap.ui.getCore().byId("addSapLogonGroup").setVisible(true)
            sap.ui.getCore().byId("addSapLogonGroupInput").setVisible(true)
            sap.ui.getCore().byId("addSapR3NameInput").setRequired(true)
            sap.ui.getCore().byId("addSapLogonGroupInput").setRequired(true)

        }else{
            sap.ui.getCore().byId("addSapIPLabel").setText("IP Address")
            sap.ui.getCore().byId("addSapMsgServerPort").setVisible(false)
            sap.ui.getCore().byId("addSapMsgServerPortInput").setVisible(false)
            sap.ui.getCore().byId("addSapMsgServerPortInput").setValue("")
            sap.ui.getCore().byId("addSapR3Name").setVisible(false)
            sap.ui.getCore().byId("addSapR3NameInput").setVisible(false)
            sap.ui.getCore().byId("addSapR3NameInput").setValue("")
            sap.ui.getCore().byId("addSapLogonGroup").setVisible(false)
            sap.ui.getCore().byId("addSapLogonGroupInput").setVisible(false)
            sap.ui.getCore().byId("addSapLogonGroupInput").setValue("")
            sap.ui.getCore().byId("addSapR3NameInput").setRequired(false)
            sap.ui.getCore().byId("addSapLogonGroupInput").setRequired(false)
        }
    },
    
    modSAProuterChange: function(oControlEvent) {
        var selItem = oControlEvent.getParameter("selectedItem").getProperty('key')
        if(selItem === "ON"){
            sap.ui.getCore().byId("modSapRouterStringLabel").setVisible(true)
            sap.ui.getCore().byId("modSapRouterStringInput").setVisible(true)
            sap.ui.getCore().byId("modSapRouterStringInput").setRequired(true)
        }else{
            sap.ui.getCore().byId("modSapRouterStringLabel").setVisible(false)
            sap.ui.getCore().byId("modSapRouterStringInput").setVisible(false)
            sap.ui.getCore().byId("modSapRouterStringInput").setValue("")
            sap.ui.getCore().byId("modSapRouterStringInput").setRequired(false)
              
        }
    },
    addSAProuterChange: function(oControlEvent) {
        var selItem = oControlEvent.getParameter("selectedItem").getProperty('key')
        if(selItem === "ON"){
            sap.ui.getCore().byId("addSapRouterStringLabel").setVisible(true)
            sap.ui.getCore().byId("addSapRouterStringInput").setVisible(true)
            sap.ui.getCore().byId("addSapRouterStringInput").setRequired(true)
        }else{
            sap.ui.getCore().byId("addSapRouterStringLabel").setVisible(false)
            sap.ui.getCore().byId("addSapRouterStringInput").setVisible(false)
            sap.ui.getCore().byId("addSapRouterStringInput").setValue("")
            sap.ui.getCore().byId("addSapRouterStringInput").setRequired(false)

        }
    }

});


