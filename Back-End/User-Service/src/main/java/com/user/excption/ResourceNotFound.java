package com.user.excption;

public class ResourceNotFound extends  RuntimeException{

  public ResourceNotFound(String msg){
      super("Not Found  "+  msg);
  }

    public ResourceNotFound(){
        super("Not such user found");
    }

}
