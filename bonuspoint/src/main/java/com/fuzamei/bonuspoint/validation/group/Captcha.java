package com.fuzamei.bonuspoint.validation.group;

public interface Captcha {
    interface MobileCaptcha extends Captcha {}
    interface EmailCaptcha extends Captcha {}
}
