package com.reporting.enums;

public enum Colors
{
  PASS("#7BB661"),  FAIL("#E03C31"),  SKIP("#21ABCD");
  
  private String color;
  
  private Colors(String paramString)
  {
    setColor(paramString);
  }
  
  public String getColor()
  {
    return this.color;
  }
  
  private void setColor(String paramString)
  {
    this.color = paramString;
  }
}


/* Location:           D:\AUT\workspace\ATUR511\ATUReporter_Selenium_testNG_5.1.1.jar
 * Qualified Name:     atu.testng.reports.enums.Colors
 * JD-Core Version:    0.7.0.1
 */