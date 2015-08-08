package com.reporting.utils;

public class AuthorDetails
{
  private String authorName = "Unknown";
  private String creationDate = "Unknown";
  private String version = "Unknown";
  
  public String getAuthorName()
  {
    return this.authorName;
  }
  
  public void setAuthorName(String paramString)
  {
    this.authorName = paramString;
  }
  
  public String getCreationDate()
  {
    return this.creationDate;
  }
  
  public void setCreationDate(String paramString)
  {
    this.creationDate = paramString;
  }
  
  public String getVersion()
  {
    return this.version;
  }
  
  public void setVersion(String paramString)
  {
    this.version = paramString;
  }
}

