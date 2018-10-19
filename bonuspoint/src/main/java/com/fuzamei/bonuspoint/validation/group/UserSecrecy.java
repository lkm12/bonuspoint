package com.fuzamei.bonuspoint.validation.group;


public interface UserSecrecy {
    interface UpdatePassword extends UserSecrecy{}
    interface UpdatePayWord extends UserSecrecy{}
    interface ResetPassword extends UserSecrecy{}
    interface UserMobile extends UserSecrecy{}
    interface UpdateMobile extends UserSecrecy{}

    interface UserEmail extends UserSecrecy{}
    interface UpdateEmail extends UserSecrecy{}


}
