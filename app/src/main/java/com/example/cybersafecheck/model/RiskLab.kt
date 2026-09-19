package com.example.cybersafecheck.model

object RiskLab {

    val items: List<RiskItem> = listOf(
        RiskItem(
            id = "pw_weak",
            question = "I use a simple password like 123456 or my birthday on my phone or accounts",
            category = RiskCategory.PASSWORDS,
            explanation = "Simple passwords such as 123456, your birthday or your name are the first ones attackers try. Automated tools can test millions of common passwords in seconds, so an account protected by a weak password can be broken into almost instantly.\n\n" +
                    "Once an attacker is in, they can read your messages, steal personal files, reset the passwords of your other accounts and even pretend to be you to scam your contacts.\n\n" +
                    "What to do: use a long passphrase made of several unrelated words, make it different for every account, and store your passwords in a password manager."
        ),
        RiskItem(
            id = "pw_mfa",
            question = "I don't use multi-factor authentication on my important accounts",
            category = RiskCategory.PASSWORDS,
            explanation = "Passwords are regularly stolen through phishing, data breaches and malware. If a password is your only protection, anyone who gets hold of it can log in as you from anywhere in the world.\n\n" +
                    "Multi-factor authentication (MFA) adds a second check, such as a one-time code, an authenticator app or a fingerprint, which the attacker does not have. Without it, one leaked password can lead to a full account takeover, including your email, banking and social media.\n\n" +
                    "What to do: turn on MFA for your email, banking, cloud storage and social media accounts, and prefer an authenticator app over SMS codes where possible."
        ),
        RiskItem(
            id = "pw_lock",
            question = "My phone has no screen lock (PIN, fingerprint or face unlock)",
            category = RiskCategory.PASSWORDS,
            explanation = "Your phone holds your banking apps, photos, messages, contacts and saved logins. If it has no screen lock, anyone who finds, steals or borrows it can open everything immediately.\n\n" +
                    "A thief could read your one-time PIN messages, open your email to reset your passwords, make payments through banking apps or wallets, and message your contacts pretending to be you.\n\n" +
                    "What to do: set a strong PIN (six digits or more) or a password, add fingerprint or face unlock for convenience, and set the phone to lock automatically after a short time."
        ),
        RiskItem(
            id = "pw_iot",
            question = "I use smart devices (speaker, lock, camera) without changing their default password",
            category = RiskCategory.PASSWORDS,
            explanation = "Smart speakers, cameras, locks, thermostats and routers often ship with well-known default passwords such as \"admin\" or \"1234\". Attackers constantly scan the internet for devices that still use them.\n\n" +
                    "A hacked smart camera can be used to spy on you, a hacked smart lock can let someone into your home, and a compromised device can be used to reach the other devices on your home network, including your phone and laptop.\n\n" +
                    "What to do: change the default password when you set the device up, use a different password for each device, keep its software updated and switch off features you don't use."
        ),
        RiskItem(
            id = "sm_id",
            question = "I post or send photos of my ID, passport or bank card online or in chats",
            category = RiskCategory.SOCIAL_MEDIA,
            explanation = "Photos of your ID, passport or bank card contain everything a criminal needs to pretend to be you: your full name, ID number, date of birth, photo and card details. Anything posted publicly can be copied, and chat apps can be hacked, forwarded or backed up in places you don't control.\n\n" +
                    "This information can be used for identity theft, such as opening accounts or taking out loans in your name, as well as for financial fraud and targeted scams against you or your family.\n\n" +
                    "What to do: never post these documents, share them only through official channels when it is really required, cover or watermark copies you must send, and delete the photos from chats and your gallery afterwards."
        ),
        RiskItem(
            id = "scam_link",
            question = "I click links in messages or emails without checking they are safe",
            category = RiskCategory.SCAMS,
            explanation = "Phishing messages pretend to come from a bank, a delivery company, a friend or your university. They use urgent wording such as \"your account will be closed\" to make you tap before you think. The link leads to a fake website that looks real, or silently installs malicious software.\n\n" +
                    "If you enter your details on the fake page, the attacker captures your username, password and card numbers. Malware can also read your messages, record what you type, and lock or steal your files.\n\n" +
                    "What to do: don't tap links you did not expect, check the sender and the full web address, and open your bank or the service by typing its address or using its official app instead."
        ),
        RiskItem(
            id = "scam_store",
            question = "I install apps from outside the official app store (Google Play or Apple App Store)",
            category = RiskCategory.SCAMS,
            explanation = "Official app stores scan apps and remove harmful ones. Apps downloaded from websites, messaging groups or unofficial stores skip these checks, and cracked or \"free\" versions of paid apps are a common way to spread malware.\n\n" +
                    "A malicious app can steal your passwords and banking details, read your messages and one-time PINs, flood you with adverts, secretly subscribe you to paid services, or give an attacker remote control of your phone.\n\n" +
                    "What to do: install apps only from the official store, check the developer name, reviews and requested permissions, and keep Play Protect or your phone's built-in scanner switched on."
        ),
        RiskItem(
            id = "scam_update",
            question = "I ignore or delay updates for my phone's operating system and apps",
            category = RiskCategory.SCAMS,
            explanation = "Software always contains bugs, and some of them are security holes that attackers can exploit. When a company finds one, it releases an update to fix it. Once the fix is public, criminals study it and target the phones that have not installed it yet.\n\n" +
                    "An out-of-date phone can be infected simply by visiting a website or opening a malicious file, and the attacker may gain access to your data or take control of the device without you noticing.\n\n" +
                    "What to do: turn on automatic updates for the operating system and your apps, install security updates as soon as they appear, and replace phones that no longer receive updates from the manufacturer."
        ),
        RiskItem(
            id = "scam_perms",
            question = "I accept app permission requests without reading what the app wants access to",
            category = RiskCategory.SCAMS,
            explanation = "When you install an app, it may ask for access to your camera, microphone, contacts, location, photos or messages. Tapping \"Allow\" on everything gives the app, and anyone who controls it, that access.\n\n" +
                    "A torch or game app that asks for your contacts or microphone may be collecting your data to sell it or to use it in scams. A malicious app with access to your SMS messages can read your one-time PINs, and one with location access can track where you live, study and travel.\n\n" +
                    "What to do: read each request, allow only what the app truly needs, choose \"only while using the app\" for location, and review and remove permissions in your phone's settings from time to time."
        ),
        RiskItem(
            id = "cb_incident",
            question = "I've received a scam or threatening message on my phone and told no one",
            category = RiskCategory.CYBERBULLYING,
            explanation = "Scam and threatening messages arrive by SMS, WhatsApp, social media and email. They can be threats, blackmail, harassment, fake prize or job offers, or people pretending to be someone you know. The senders rely on you staying quiet, embarrassed or afraid.\n\n" +
                    "Staying silent lets the abuse continue and often get worse. Scammers keep targeting people who have replied, and bullies or blackmailers can cause serious stress, damage to your reputation and financial loss.\n\n" +
                    "What to do: don't reply or pay anything, take screenshots as evidence, block and report the sender on the platform, and tell someone you trust, your university or the police, especially if there are threats."
        )
    )

    fun getItem(id: String): RiskItem? = items.find { it.id == id }
}