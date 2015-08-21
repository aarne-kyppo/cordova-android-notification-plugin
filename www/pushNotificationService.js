var exec = require('cordova/exec');

var pushNotificationService = {
  initialize: function(success,error){
    this.service = "pushNotificationService";
    //Default icon to ensure that notificationcompat will get icon which is mandatory.
    this.icongroup = "drawable";
    this.iconkey = "icon";
    exec(success,error,this.service,"initialize",[this.iconkey,this.icongroup]);
  },
  sendNotification: function(success,error, title, message,data){
    exec(success, error, this.service, "sendNotification", [title, message,data]);
  },
  setIcon: function(key,group){
    if(key){
      this.iconkey = key;
    }
    if(group){
      this.group = group;
    }
  }
};

module.exports = pushNotificationService;
