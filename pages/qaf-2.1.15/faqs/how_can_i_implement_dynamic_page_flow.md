---
title: How can I implement dynamic page flow using page hierarchy design?
sidebar: faq_sidebar
permalink: qaf-2.1.15/how_can_i_implement_dynamic_page_flow.html
folder: qaf-2.1.15
---

Consider the following case where page flow is configurable in AUT.

According to flow one page flow is Review Page -> Passenger Page – > Payment Page

Another possible flow is Review Page -> Payment Page.

{% include inline_image.html file="Dynamic_Linked_Pages.png" alt="Dynamic Linked Pages " %}

Blow sample shows you the implementation for such case.

```java

public class ReviewFlightPage extends WebDriverBaseTestPage<ReviewPage> implements PaymentLocators, PaymentsPageLauncher {

}

```

```java
 
public class PassengerPage extends WebDriverBaseTestPage<ReviewFlightPage> implements PaymentLocators, PaymentsPageLauncher {

}

```

```java
 
public class PaymentPage extends WebDriverBaseTestPage<PaymentsPageLauncher> implements PaymentLocators{

  protecte void initParent() {
  
    this.parent= (pageProps.getInt(“review.next.flow”) == 6)
    
    ? new ReviewFlightPage()
    
    : new PassengerPage();
  
  }

}

```

 
