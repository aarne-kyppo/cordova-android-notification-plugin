var exec = require('cordova/exec');

var pushNotificationService = {
  initialize: function(){
    this.service = "pushNotificationService";
    //Default icon to ensure that notificationcompat will get icon which is mandatory.
    this.icongroup = "drawable";
    this.iconkey = "icon";
    exec(this.notificationsuccess,this.error,this.service,"initialize",[this.iconkey,this.icongroup]);
  },
  coolMethod: function(arg0) {
      exec(this.notificationsuccess, this.error, this.service, "coolMethod", [arg0]);//Not so DRY. curry method will be implemented.
  },
  sendNotification: function(title, message){
    exec(this.notificationsuccess, this.error, this.service, "sendNotification", [title, message]);
  },
  setIcon: function(key,group){
    if(key){
      this.iconkey = key;
    }
    if(group){
      this.group = group;
    }
  },
  success: function(){
  },
  notificationsuccess: function(data){
    if(data){
      alert(data.msg);
    }
  },
  error: function(errmessage){
    alert(errmessage);
  },
};

module.exports = pushNotificationService;
