/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

jQuery.sap.require("sap.m.Panel");
jQuery.sap.require("sap.ui.table.Table");

sap.ui.jsview("swv.modules.config.Config", {
    /**
     * Specifies the Controller belonging to this View.
     * In the case that it is not implemented, or that "null" is returned, this View does not have a Controller.
     */
    getControllerName: function() {
        return "swv.modules.config.Config";
    },
    init: function() {

        this.createId("Configview");

    },
    /**
     * Is initially called once after the Controller has been instantiated. It is the place where the UI is constructed.
     * Since the Controller is given to this method, its event handlers can be attached right away.
     */
    createContent: function(oController) {
        var message=new sap.m.Label({
            id:"mesasage_Mandatory33",
            text:"{i18n>message.mandotory}"
        });
        var oLayout1 = new sap.ui.layout.form.ResponsiveGridLayout("confL1", {
            labelSpanXL: 5,
            labelSpanL: 7,
            labelSpanM: 4,
            labelSpanS: 12,
            emptySpanL: 1,
            emptySpanM: 1,
            emptySpanS: 1,
            columnsL: 1,
            columnsM: 1,
            breakpointXL: 1000,
            breakpointL: 600,
            breakpointM: 400
        });

        var oForm1 = new sap.ui.layout.form.Form("confF1",{
            title: new sap.ui.core.Title({
                text: "Admin Login"

            }),
            editable: true,
            layout: oLayout1,
            formContainers: [
            new sap.ui.layout.form.FormContainer("confF1C1",{

                formElements: [
                new sap.ui.layout.form.FormElement("confF1C1E1",{
                    label: "Username",
                    fields: [new sap.m.Input({
                        id:"confText",
                        liveChange:[oController.liveChangeAction,oController],
                        layoutData: new sap.ui.layout.GridData({
                            span: "XL2 L3 M3 S12"
                        })
                    })
                    ]
                }),
                new sap.ui.layout.form.FormElement("F1C1E545",{
                    label: "",
                    visible:false,
                    fields: [message
                    ]
                }),
                new sap.ui.layout.form.FormElement("confF1C1E2",{
                    label: "Password",
                    fields: [new sap.m.Input({
                        id:"confpassText",
                        type :"Password",
                        submit :[oController.admin_Login_butt_action,oController],
                        liveChange:[oController.liveChangeAction,oController],
                        layoutData: new sap.ui.layout.GridData({
                            span: "XL2 L3 M3 S12"
                        })
                    })
                    ]
                }),
                new sap.ui.layout.form.FormElement("confF1C1E4",{
                    label: "",
                    fields: [new sap.m.Image({
                        id:"id_config_image",
                        //src: "jcaptcha.jpg",
                        src: "",//Image will set of Config tab click
                        layoutData: new sap.ui.layout.GridData({
                            span: "XL2 L3 M3 S12"
                        }),
                        load:function(oEvent){
                            sap.ui.getCore().byId("id_Config_jCaptcha").setBusy(false);
                            sap.ui.getCore().byId("id_Config_jCaptcha").setValue("");
                        },
                        error:function(oEvent){
                            //oEvent.getSource().setSrc("adminCaptcha.jpg");
                            if (sap.ui.getCore().byId("id_config_image").getSrc() !== "") {
                                console.log("error admin login rendering captcha");
                            }
                        }
                    }),
                    new sap.m.Button({
                        id:"id_Config_Refresh",
                        enabled: true,
                        tooltip: "Refresh Captcha Image",
                        icon:"sap-icon://refresh",
                        type: "Emphasized",
                        width: "33%",
                        visible:true,
                        press: [oController.admin_Login_captcha_refresh,oController],
                        layoutData : new sap.ui.layout.GridData({
                            span: "XL2 L1 M1 S12"
                        })
                    })
                    ]
                }),
                new sap.ui.layout.form.FormElement("confF1C1E5",{
                    label: "Captcha",
                    fields: [new sap.m.Input({
                        id:"id_Config_jCaptcha",
                        value: "wait...",
                        visible:true,
                        busy: true,
                        busyIndicatorDelay: 0,
                        maxLength: 6,
                        submit :[oController.admin_Login_butt_action,oController],
                        liveChange:[oController.liveChangeAction,oController],
                        layoutData: new sap.ui.layout.GridData({
                            span: "XL2 L3 M6 S12"
                        }),
                        
                    })
                    ]
                }),
                new sap.ui.layout.form.FormElement("confF1C1E3",{
                    label:"",
                    fields:[
                    new sap.m.Button({
                        text:"Submit",
                        icon:"sap-icon://action",
                        type: "Emphasized",
                        press: [oController.admin_Login_butt_action,oController],
                        layoutData : new sap.ui.layout.GridData({
                            span: "XL2 L3 M6 S12"
                        })
                    })
                    ]
                })
                ]
            })

            ]
        });

               
        this.addStyleClass("sapUiSizeCompact");


        /**
         *
         * new view code table layout of configuration
         *
         */
        var verticalLayout1 = new sap.ui.layout.VerticalLayout("verticalConfview",{
            width:"100%"
        });
        var ConfToolbar1 = new sap.m.Toolbar("ADConfTool");

        var ConfTitle1 = new sap.m.Title("ADConfTitle",{
            text : "Ad Server"
        })
        var ADToolbarSpacer = new sap.m.ToolbarSpacer("ADToolSpacer");
        var ADButton = new sap.m.Button("ADAddBtn",{
            icon : "sap-icon://add",
            tooltip : "Add New",
            text : "Add New",
            type: "Emphasized",
            press:[oController.ADD_AD_Action,oController]

        });
        var ADButton1 = new sap.m.Button("ADEditBtn",{
            icon : "sap-icon://edit",
            tooltip : "Modify",
            text : "Modify",
            type: "Emphasized",
            press:[oController.modify_AD_Action,oController]

        });
        var ADButton2 = new sap.m.Button("ADDeleteBtn",{
            icon : "sap-icon://delete",
            tooltip : "Delete",
            text : "Delete",
            type: "Emphasized",
            press:[oController.delete_AD_Action,oController]

        });
        var ADButton3 = new sap.m.Button("ADTestBtn",{
            icon : "sap-icon://activate",
            tooltip : "Test Connection",
            text : "Test Connection",
            type: "Emphasized",
            press:[oController.test_AD_Action,oController]

        });

        ConfToolbar1.addContent(ConfTitle1);
        ConfToolbar1.addContent(ADToolbarSpacer);
        ConfToolbar1.addContent(ADButton);
        ConfToolbar1.addContent(ADButton1);
        ConfToolbar1.addContent(ADButton2);
        ConfToolbar1.addContent(ADButton3);

        /**
         *
         * panel for AD server
         *
         */
        var panel1 = new sap.m.Panel({
            id:"ActiveDIRPanel",
            headerToolbar : ConfToolbar1,
            expandable : true,
            expanded :false,
            width:"100%",
            expand :[oController.ADExpandAction,oController]
        });
        panel1.addStyleClass("paneladmincontent");
        panel1.addStyleClass("headerPanelBackgroundColor");
        var adTable= new sap.ui.table.Table("adTable",{
            selectionMode: sap.ui.table.SelectionMode.Single,
            selectionBehavior: sap.ui.table.SelectionBehavior.Row,
            columnHeaderHeight: 25
        });
        var adColumn = new sap.ui.table.Column({
            label: new  sap.m.Label({
                text: "Domain"
            }),
            template: new sap.m.Label().bindProperty("text","domain"),
            width: "auto"
        });
        var adColumn1 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "IP Address"
            }),
            template: new sap.m.Label().bindProperty( "text","ip"),
            width: "auto"
        });
        var adColumn2 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Port"
            }),
            template: new sap.m.Label().bindProperty( "text","port"),
            width: "auto"
        });
        var adColumn3 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "User ID"
            }),
            visible: false,
            template: new sap.m.Label().bindProperty( "text","userid"),
            width: "auto"
        });


        adTable.addColumn(adColumn);
        adTable.addColumn(adColumn1);
        adTable.addColumn(adColumn2);
        adTable.addColumn(adColumn3);
        panel1.addContent(adTable);


        //ADD dialog AD system
        var ADAddgridLayout = new sap.ui.layout.Grid("addADGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [
            new sap.m.Label({
                id:"addADDomainLabel",
                text: "Domain",
                labelFor : "addADDomaininput",

                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addADDomaininput",
                width: "100%",
                liveChange:[oController.livechange_Add_AD,oController],
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S12"
                })
            }),


            new sap.m.Label({
                id:"addADIPLabel",
                text: 'IP Address',
                labelFor : "addADIPInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addADIPInput",
                width: "100%",
                required: true,
                liveChange:[oController.livechange_Add_AD,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addADPortLabel",
                text: 'Port',
                labelFor : "addADPortInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addADPortInput",
                width: "100%",
                required: true,
                liveChange:[oController.livechange_Add_AD,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"addADUserIDLabel",
                text: 'User ID',
                labelFor : "addADUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addADUserIDInput",
                width: "100%",
                required: true,
                liveChange:[oController.livechange_Add_AD,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addADPassLabel",
                text: 'Password',
                labelFor : "addADPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addADPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.livechange_Add_AD,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.submitAdd_AD_Action,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press:  function(){
                    sap.ui.getCore().byId("ADAddDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        var ADAddDialog = new sap.m.Dialog({
            id:"ADAddDialog",
            title:"ADD New AD Server"
            //width:"30%"
        });
        ADAddDialog.addStyleClass("sappanel");

        ADAddgridLayout.addStyleClass("sapUiSizeCompact");
        ADAddDialog.addContent(ADAddgridLayout);



        //modify dialog AD system
        var ADModgridLayout = new sap.ui.layout.Grid("modADGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [
            new sap.m.Label({
                id:"modADDomainLabel",
                text: "Domain",
                labelFor : "modADDomaininput",

                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modADDomaininput",
                width: "100%",
                
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S12"
                })
            }),


            new sap.m.Label({
                id:"modADIPLabel",
                text: 'IP Address',
                labelFor : "modADIPInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modADIPInput",
                width: "100%",
                enabled:false,
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modADPortLabel",
                text: 'Port',
                labelFor : "modADPortInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modADPortInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"modADUserIDLabel",
                text: 'User ID',
                labelFor : "modADUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modADUserIDInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modADPassLabel",
                text: 'Password',
                labelFor : "modADPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modADPassInput",
                width: "100%",
                type:"Password",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
           
            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.Submit_modify_AD_Action,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press: function(){
                    sap.ui.getCore().byId("ADModDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        var ADModDialog = new sap.m.Dialog({
            id:"ADModDialog",
            title:"Modify AD Server"
            //width:"30%"
        });
        ADModDialog.addStyleClass("sappanel");

        ADModgridLayout.addStyleClass("sapUiSizeCompact");
        ADModDialog.addContent(ADModgridLayout);


        /**
         *
         * panel for admin configuration
         *
         */
        var ConfToolbar2 = new sap.m.Toolbar("loginConfTool");

        var ConfTitle2 = new sap.m.Title("adninlogintableTitle",{
            text : "Admin Login"
        });
        var AdmToolbarSpacer = new sap.m.ToolbarSpacer("AdmToolSpacer");
        var AdmButton = new sap.m.Button("AdmAddBtn",{
            icon : "sap-icon://add",
            tooltip : "Add New",
            text : "Add New",
            type: "Emphasized",
            press:[oController.add_admin_Action,oController]
        });
        var AdmButton1 = new sap.m.Button("AdmEditBtn",{
            icon : "sap-icon://edit",
            tooltip : "Modify",
            text : "Modify",
            type: "Emphasized",
            press:[oController.modify_admin_Action,oController]

        });
        var AdmButton2 = new sap.m.Button("AdmDeleteBtn",{
            icon : "sap-icon://delete",
            tooltip : "Delete",
            text : "Delete",
            type: "Emphasized",
            press:[oController.delete_admin_Action,oController]

        });
       

        ConfToolbar2.addContent(ConfTitle2);
        ConfToolbar2.addContent(AdmToolbarSpacer);
        ConfToolbar2.addContent(AdmButton);
        ConfToolbar2.addContent(AdmButton1);
        ConfToolbar2.addContent(AdmButton2);
       
        var panel2 = new sap.m.Panel({
            id:"adminPanel",
            headerToolbar : ConfToolbar2,
            expandable : true,
            expanded :false,
            width:"100%",
            expand :[oController.adminExpandAction,oController]
        });
        panel2.addStyleClass("paneladmincontent");
        panel2.addStyleClass("headerPanelBackgroundColor");
        var loginTable= new sap.ui.table.Table("loginTable",{
            selectionMode: sap.ui.table.SelectionMode.Single,
            selectionBehavior: sap.ui.table.SelectionBehavior.Row,
            columnHeaderHeight: 25
        });
        var loginColumn = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Admin user"
            }),
            template: new sap.m.Label().bindProperty( "text","userID"),
            width: "400px"
        });
        
        loginTable.addColumn(loginColumn);
        //        var data1=[{
        //            user:"admin"
        //        }
        //        ];
        //        var oModel1 = new sap.ui.model.json.JSONModel();
        //        oModel1.setData({
        //            adminlogin: data1
        //        });
        //        loginTable.setModel(oModel1);
        //        loginTable.bindRows("/adminlogin");
        panel2.addContent(loginTable);
        //ADD dialog Admin system
        var AdminAddgridLayout = new sap.ui.layout.Grid("addAdminGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [


            new sap.m.Label({
                id:"addAdminUserIDLabel",
                text: 'User ID',
                labelFor : "addAdminUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addAdminUserIDInput",
                width: "100%",
                required: true,
                liveChange:[oController.liveChangeADDAdmin,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addAdminPassLabel",
                text: 'Password',
                labelFor : "addAdminPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addAdminPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.liveChangeADDAdmin,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addAdminCnfPassLabel",
                text: 'Confirm Password',
                labelFor : "addAdminCnfPassInput",

                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addAdminCnfPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.liveChangeADDAdmin,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.add_admin_SumbitAction,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press: function(){
                    sap.ui.getCore().byId("AdminAddDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        var AdminAddDialog = new sap.m.Dialog({
            id:"AdminAddDialog",
            title:"ADD New Admin User"
            //width:"30%"
        });
        AdminAddDialog.addStyleClass("sappanel");

        AdminAddgridLayout.addStyleClass("sapUiSizeCompact");
        AdminAddDialog.addContent(AdminAddgridLayout);
        //Modify dialog Admin system
        var AdminModgridLayout = new sap.ui.layout.Grid("modAdminGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [


            new sap.m.Label({
                id:"modAdminUserIDLabel",
                text: 'User ID',
                labelFor : "modAdminUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modAdminUserIDInput",
                width: "100%",
                required: true,
                enabled:false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modAdminPassLabel",
                text: 'Password',
                labelFor : "modAdminPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modAdminPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.liveChangeModifyadmin,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modAdminCnfPassLabel",
                text: 'Confirm Password',
                labelFor : "modAdminCnfPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modAdminCnfPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.liveChangeModifyadmin,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.modify_admin_SubmitAction,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press: function(){
                    sap.ui.getCore().byId("AdminModDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        var AdminModDialog = new sap.m.Dialog({
            id:"AdminModDialog",
            title:"Modify Admin"
            //width:"30%"
        });
        AdminModDialog.addStyleClass("sappanel");

        AdminModgridLayout.addStyleClass("sapUiSizeCompact");
        AdminModDialog.addContent(AdminModgridLayout);
        /**
         *
         * panel for SAP SERVER
         *
         */
        var ConfToolbar3 = new sap.m.Toolbar("sapConfTool");

        var ConfTitle3 = new sap.m.Title("sapConfTitle",{
            text : "SAP Server"
        })
        var sapToolbarSpacer = new sap.m.ToolbarSpacer("SapToolSpacer");
        
        var syncButton = new sap.m.Button("syncBtn",{
            icon : "sap-icon://synchronize",
            tooltip : "Synchronize RP Configuration",
            text : "Config. Sync",
            type: "Emphasized",
            press:[oController.syncButton_Action,oController]

        });
        
        var appButton = new sap.m.Button("sapAddBtn",{
            icon : "sap-icon://add",
            tooltip : "Add New",
            text : "Add New",
            type: "Emphasized",
            press:[oController.add_sap_butt_action,oController]

        });
        var appButton1 = new sap.m.Button("sapEditBtn",{
            icon : "sap-icon://edit",
            tooltip : "Modify",
            text : "Modify",
            type: "Emphasized",
            press:[oController.modify_SAP_Action,oController]

        });
        var appButton2 = new sap.m.Button("sapDeleteBtn",{
            icon : "sap-icon://delete",
            tooltip : "Delete",
            text : "Delete",
            type: "Emphasized",
            press:[oController.delete_SAP_Action,oController]

        });
        var appButton3 = new sap.m.Button("sapTestBtn",{
            icon : "sap-icon://activate",
            tooltip : "Test Connection",
            text : "Test Connection",
            type: "Emphasized",
            press:[oController.test_SAP_Action,oController]

        });
      
        ConfToolbar3.addContent(ConfTitle3);
        ConfToolbar3.addContent(sapToolbarSpacer);
        ConfToolbar3.addContent(syncButton);
        ConfToolbar3.addContent(appButton);
        ConfToolbar3.addContent(appButton1);
        ConfToolbar3.addContent(appButton2);
        ConfToolbar3.addContent(appButton3);

        var panel3 = new sap.m.Panel({
            id:"sapPanel",
            headerToolbar : ConfToolbar3,
            expandable : true,
            expanded :false,
            width:"100%",
            expand :[oController.sapExpandAction,oController]
        });
        panel3.addStyleClass("paneladmincontent");
        panel3.addStyleClass("headerPanelBackgroundColor");
        var sapTable= new sap.ui.table.Table("sapTable",{
            selectionMode: sap.ui.table.SelectionMode.Single,
            selectionBehavior: sap.ui.table.SelectionBehavior.Row,
            columnHeaderHeight: 25
        });
        var sapColumn = new sap.ui.table.Column({
            label: new  sap.m.Label({
                text: "System Name"
            }),
            template: new sap.m.Label().bindProperty("text","name"),
            width: "auto"
        });
        var sapColumn1 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "RFC Name"
            }),
            template: new sap.m.Label().bindProperty( "text","rfcname"),
            width: "auto"
        });
        var sapColumn2 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "HOST"
            }),
            template: new sap.m.Label().bindProperty( "text","host"),
            width: "auto"
        });
        var sapColumn3 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "HOST Name/IP Address"
            }),
            template: new sap.m.Label().bindProperty( "text","ipaddress"),
            width: "auto"
        });
        var sapColumn4 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "System Number"
            }),
            template: new sap.m.Label().bindProperty( "text","syno"),
            width: "auto"
        });
        var sapColumn5 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Client"
            }),
            template: new sap.m.Label().bindProperty( "text","client"),
            width: "auto"
        });
        var sapColumn6 = new sap.ui.table.Column({
            label: new sap.m.Label({
                //text: "Client"
                text: "SNC"
            }),
            //template: new sap.m.Label().bindProperty( "text","client"),
            template: new sap.m.Label().bindProperty( "text","sncFlag"),
            width: "10%"
        });
        var sapColumn7 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "User ID"
            }),
            visible: false,
            template: new sap.m.Label().bindProperty( "text","user"),
            width: "auto"
        });
        var sapColumn8 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Language"
            }),
            template: new sap.m.Label().bindProperty( "text","lang"),
            width: "auto"
        });
        var sapColumn9 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Backup System"
            }),
            template: new sap.m.Label().bindProperty( "text","backup"),
            width: "auto"
        });
        var sapColumn10 = new sap.ui.table.Column({
            label: new sap.m.Label({
                text: "Non sap System"
            }),
            template: new sap.m.Label().bindProperty( "text","enflag"),
            width: "auto"
        });


        sapTable.addColumn(sapColumn);
        sapTable.addColumn(sapColumn1);
        sapTable.addColumn(sapColumn2);
        sapTable.addColumn(sapColumn3);
        sapTable.addColumn(sapColumn4);
        sapTable.addColumn(sapColumn5);
        sapTable.addColumn(sapColumn6);
        sapTable.addColumn(sapColumn7);
        sapTable.addColumn(sapColumn8);
        sapTable.addColumn(sapColumn9);
        sapTable.addColumn(sapColumn10);


       
        panel3.addContent(sapTable);
        var queTemplate= new sap.ui.core.Item({
            key:"{Name}",
            text:"{Value}"
        });
        var queTemplate1= new sap.ui.core.Item({
            key:"{Name}",
            text:"{Value}"
        });
        var connTypeData=[{
            "Name":"NO",
            "Value":"Direct"
        }, {
            "Name":"YES",
            "Value":"Load Balanced"
        }];
        var dataflag=[{
            "Name":"Select",
            "Value":"Select"
        }, {
            "Name":"OFF",
            "Value":"OFF"
        },{
            "Name":"ON",
            "Value":"ON"
        }];
        var dataflag2=[
        {
            "Name":"OFF",
            "Value":"OFF"
        },{
            "Name":"ON",
            "Value":"ON"
        }];
        var backup=[{
            "Name":"Select",
            "Value":"Select"
        }, {
            "Name":"YES",
            "Value":"YES"
        },{
            "Name":"NO",
            "Value":"NO"
        }];
    
         var sncflag=[{
            "Name":"OFF",
            "Value":"OFF"
        },{
            "Name":"ON",
            "Value":"ON"
        }];
    
        var sncflagModel=new sap.ui.model.json.JSONModel();
        sncflagModel.setData(sncflag);
        
        var backupModel=new sap.ui.model.json.JSONModel();
        backupModel.setData(backup);

        var oModelList = new sap.ui.model.json.JSONModel();
        oModelList.setData(dataflag);
        var oModelList2 = new sap.ui.model.json.JSONModel();
        oModelList2.setData(dataflag2);
        var oModelConnType = new sap.ui.model.json.JSONModel();
        oModelConnType.setData(connTypeData);
        
        var gridLayout = new sap.ui.layout.Grid("ConfGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [
            new sap.m.Label({
                id:"addSapSysNameLabel",
                text: "System Name ",
                labelFor : "addSapSysNameinput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSysNameinput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S12"
                })
            }),
            new sap.m.Label({
                id:"addSapConnTypeLabel",
                text: 'Connection Type ',
                labelFor : "addSapConnTypeSelect",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"addSapConnTypeSelect",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",

                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                }),
                //required: false,
                change: [oController.addConnectionTypeChange,oController]

            }),
            new sap.m.Label({
                id:"addSapIPLabel",
                text: 'IP Address',
                labelFor : "addSapIPInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapIPInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapMsgServerPort",
                visible: false,
                text: 'Message Service(Optional)',
                labelFor : "addSapMsgServerPortInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapMsgServerPortInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"addSapR3Name",
                visible: false,
                text: 'R3 Name/System ID',
                labelFor : "addSapR3NameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapR3NameInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"addSapLogonGroup",
                visible: false,
                text: 'Logon Group',
                labelFor : "addSapLogonGroupInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapLogonGroupInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapISrouterStringLabel",
                text: 'Router flag',
                labelFor : "addSapISrouterStringInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"addSapISrouterStringInput",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",

                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                }),
                change: [oController.addSAProuterChange,oController]

            }),
            new sap.m.Label({
                id:"addSapRouterStringLabel",
                text: 'SAP Router String',
                visible: false,
                labelFor : "addSapRouterStringInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapRouterStringInput",
                width: "100%",
                required: false,
                visible: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapRfcNameLabel",
                text: 'RFC Name',
                labelFor : "addSapRfcNameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapRfcNameInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapHostLabel",
                text: 'Host Flag',
                labelFor : "addSapHostInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"addSapHostInput",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",
                change :[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })

            }),
            
            new sap.m.Label({
                id:"addSapSysNoLabel",
                text: 'System Number',
                labelFor : "addSapSysNoInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSysNoInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapClientLabel",
                text: 'Client',
                labelFor : "addSapClientInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapClientInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapUserIDLabel",
                text: 'User ID',
                labelFor : "addSapUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapUserIDInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapPassLabel",
                text: 'Password',
                labelFor : "addSapPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapPassInput",
                width: "100%",
                type:"Password",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapLangLabel",
                text: 'Language',
                labelFor : "addSapLangInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapLangInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapBackupLabel",
                text: 'Backup Flag',
                labelFor : "addSapBackupInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"addSapBackupInput",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",
                change :[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })

            }),
            new sap.m.Label({
                id:"addSapNonSapSysLabel",
                text: 'Non SAP System',
                labelFor : "addSapNonSapSysInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapNonSapSysInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapSncFlagLabel",
                text: 'Snc Flag',
                visible:true,
                labelFor : "addSapSncInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"addSapSncFlagInput",
                visible:true,
                //required: true,
                items: {
                    path: "/",
                    template: queTemplate1
                },
                width:"100%",
                 change :[oController.add_Sap_SncString,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })

            }),

            new sap.m.Label({
                id:"addSapSncModeLabel",
                text: 'Snc Mode',
                visible:true,
                labelFor : "addSapSncModeInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSncModeInput",
                width: "100%",
                required: true,
                visible:true,
                //      liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapSncNameLabel",
                text: 'Snc Name',
                visible:true,
                labelFor : "addSapSncNameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSncNameInput",
                width: "100%",
              required: true,
                visible:true,
                //      liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapSncServiceLabel",
                text: 'Snc Service',
                visible:true,
                labelFor : "addSapSncServiceInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSncServiceInput",
                width: "100%",
                required: true,
                visible:true,
                //      liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapSncPartnerLabel",
                text: 'Snc Partner',
                visible:true,
                labelFor : "addSapSncPartnerInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSncPartnerInput",
                width: "100%",
                required: true,
                visible:true,
                //      liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"addSapSncLevelLabel",
                text: 'Snc Level',
                visible:true,
                labelFor : "addSapSncLevelInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"addSapSncLevelInput",
                width: "100%",
                required: true,
                visible:true,
                //      liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
                                   
            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.add_SAP_submitAction,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press:function(){
                    sap.ui.getCore().byId("SapAddDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        sap.ui.getCore().byId("addSapConnTypeSelect").setModel(oModelConnType);
        sap.ui.getCore().byId("addSapHostInput").setModel(oModelList);
        sap.ui.getCore().byId("addSapBackupInput").setModel(backupModel);
        sap.ui.getCore().byId("addSapSncFlagInput").setModel(sncflagModel); 
        var sapAddDialog = new sap.m.Dialog({
            id:"SapAddDialog",
            title:"ADD New System"
            //width:"30%"
        });
        sapAddDialog.addStyleClass("sappanel");

        gridLayout.addStyleClass("sapUiSizeCompact");
        sapAddDialog.addContent(gridLayout);

        //modify dialog sap system
        var sapModgridLayout = new sap.ui.layout.Grid("modConfGridId",{
            hSpacing: 1,
            vSpacing: 1,
            content: [
            new sap.m.Label({
                id:"modSapSysNameLabel",
                text: "System Name ",
                labelFor : "modSapSysNameinput",

                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSysNameinput",
                width: "100%",
                enabled:false,
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S12"
                })
            }),
            new sap.m.Label({
                id:"modSapConnTypeLabel",
                text: 'Connection Type ',
                labelFor : "modSapConnTypeSelect",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"modSapConnTypeSelect",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",

                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                }),
                //required: false,
                change: [oController.modConnectionTypeChange,oController]

            }),
            new sap.m.Label({
                id:"modSapIPLabel",
                text: 'IP Address',
                labelFor : "modSapIPInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapIPInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
             
            new sap.m.Label({
                id:"modSapMsgServerPort",
                visible: false,
                text: 'Message Service(Optional)',
                labelFor : "modSapMsgServerPortInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapMsgServerPortInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapR3Name",
                visible: false,
                text: 'R3 Name/System ID',
                labelFor : "modSapR3NameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapR3NameInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapLogonGroup",
                visible: false,
                text: 'Logon Group',
                labelFor : "modSapLogonGroupInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapLogonGroupInput",
                visible: false,
                width: "100%",
                required: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"modSapISrouterStringLabel",
                text: 'Router flag',
                labelFor : "modSapISrouterStringInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"modSapISrouterStringInput",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",

                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                }),
                change: [oController.modSAProuterChange,oController]

            }),
            new sap.m.Label({
                id:"modSapRouterStringLabel",
                text: 'SAP Router String',
                visible: false,
                labelFor : "modSapRouterStringInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapRouterStringInput",
                width: "100%",
                required: false,
                visible: false,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),

            new sap.m.Label({
                id:"modSapRfcNameLabel",
                text: 'RFC Name',
                labelFor : "modSapRfcNameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapRfcNameInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapHostLabel",
                text: 'Host Flag',
                labelFor : "modSapHostInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"modSapHostInput",
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",

                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })

            }),
            
            new sap.m.Label({
                id:"modSapSysNoLabel",
                text: 'System Number',
                labelFor : "modSapSysNoInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSysNoInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapClientLabel",
                text: 'Client',
                labelFor : "modSapClientInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapClientInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapUserIDLabel",
                text: 'User ID',
                labelFor : "modSapUserIDInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapUserIDInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapPassLabel",
                text: 'Password',
                labelFor : "modSapPassInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapPassInput",
                width: "100%",
                type:"Password",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapLangLabel",
                text: 'Language',
                labelFor : "modSapLangInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapLangInput",
                width: "100%",
                required: true,
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapBackupLabel",
                text: 'Backup Flag',
                labelFor : "modSapBackupInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"modSapBackupInput",
                enabled:false,
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })

            }),
            new sap.m.Label({
                id:"modSapNonSapSysLabel",
                text: 'Non SAP System',
                labelFor : "modSapNonSapSysInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapNonSapSysInput",
                width: "100%",
                required: true,
                liveChange:[oController.preValidation_add_Sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            
            new sap.m.Label({
                id:"modSapSncFlagLabel",
                text: 'SNC Flag',
                labelFor : "modSapSncFlagInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Select({
                id:"modSapSncFlagInput",
                enabled:true,
                items: {
                    path: "/",
                    template: queTemplate
                },
                width:"100%",
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                }),
                change:[oController.modify_Sap_SncString,oController]
            }),
            new sap.m.Label({
                id:"modSapSncModeLabel",
                text: 'Snc Mode',
                labelFor : "modSapSncModeInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSncModeInput",
                width: "100%",
                required: true,
                //       liveChange:[oController.preValidation_mod_sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapSncNameLabel",
                text: 'Snc Name',
                labelFor : "modSapSncNameInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSncNameInput",
                width: "100%",
                required: true,
                //       liveChange:[oController.preValidation_mod_sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapSncServiceLabel",
                text: 'Snc Service',
                labelFor : "modSapSncServiceInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSncServiceInput",
                width: "100%",
                required: true,
                //       liveChange:[oController.preValidation_mod_sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapSncPartnerLabel",
                text: 'Snc Partner',
                labelFor : "modSapSncPartnerInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSncPartnerInput",
                width: "100%",
                required: true,
                //       liveChange:[oController.preValidation_mod_sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
            new sap.m.Label({
                id:"modSapSncLevelLabel",
                text: 'Snc Level',
                labelFor : "modSapSncLevelInput",
                layoutData : new sap.ui.layout.GridData({
                    span: "L4 M3 S12"
                })
            }),
            new sap.m.Input({
                id:"modSapSncLevelInput",
                width: "100%",
                required: true,
                //       liveChange:[oController.preValidation_mod_sap,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L8 M9 S10"
                })
            }),
           
            
            new sap.m.Button({
                text:"Submit",
                icon:"sap-icon://action",
                type: "Emphasized",
                press: [oController.Submit_modify_SAP_Action,oController],
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            }),
            new sap.m.Button({
                text:"Cancel",
                icon:"sap-icon://sys-cancel",
                type: "Emphasized",
                press:function(){
                    sap.ui.getCore().byId("SapModDialog").close();
                },
                layoutData : new sap.ui.layout.GridData({
                    span: "L6 M5 S6"
                })
            })
            ]
        });
        sap.ui.getCore().byId("modSapConnTypeSelect").setModel(oModelConnType);
        sap.ui.getCore().byId("modSapISrouterStringInput").setModel(oModelList2);
        sap.ui.getCore().byId("addSapISrouterStringInput").setModel(oModelList2);
        sap.ui.getCore().byId("modSapHostInput").setModel(oModelList);
        sap.ui.getCore().byId("modSapBackupInput").setModel(backupModel);
        sap.ui.getCore().byId("modSapSncFlagInput").setModel(sncflagModel);        
        //        gridLayout.setModel(oModelList);
        var sapModDialog = new sap.m.Dialog({
            id:"SapModDialog",
            title:"Modify System"
            //width:"30%"
        });
        sapModDialog.addStyleClass("sappanel");

        sapModgridLayout.addStyleClass("sapUiSizeCompact");
        sapModDialog.addContent(sapModgridLayout);


        verticalLayout1.addContent(panel3);
        verticalLayout1.addContent(panel1);
        verticalLayout1.addContent(panel2);
        return oForm1;

    }


});
