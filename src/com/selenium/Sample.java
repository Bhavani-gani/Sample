package com.selenium;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.base.Verify;

public class Sample 
{
	WebDriver driver;
	@BeforeMethod
	public void setUp()
	{
		System.setProperty("webdriver.chrome.driver","E:\\EclipseNew\\MyEclipseNew\\Assignment\\drivers\\chromedriver.exe");
		driver=new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(5000,TimeUnit.SECONDS);
		driver.manage().timeouts().pageLoadTimeout(4000,TimeUnit.SECONDS);
	}
	@Test
	public void checkingData() throws Exception
	{
		//Step 1:Open the page https://en.wikipedia.org/wiki/...
		driver.get("https://en.wikipedia.org/wiki/Selenium");
		
		//Step 2: Verify that the external links in “External links“ section work
		Thread.sleep(5000);
		List<WebElement> links=driver.findElements(By.xpath("(//a//span[@class='toctext'])[33]"));
		for(int i=0;i<links.size();i++)
		{
			String str=links.get(i).getText();
			try
			{
				
				if(str.equals("External links"))
				{
				links.get(i).click();
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		Thread.sleep(5000);
		
		//Step 3: Click on the “Oxygen” link on the Periodic table at the bottom of the page
		WebElement element=driver.findElement(By.xpath("(//a[@title='Oxygen'])[2]"));
		try
		{
		JavascriptExecutor js=((JavascriptExecutor)driver);
		js.executeScript("arguments[0].scrollIntoView(true);",element);
		}
		catch(Exception e)
		{
			System.out.println("element not visible to scorll"+e.getMessage() );
		}
		element.click();
		
		//Step 4: Verify that it is a “featured article”
		String str=driver.findElement(By.xpath("//a[@title='This is a featured article. Click here for more information.']")).getAttribute("title");
		Assert.assertEquals(str,"This is a featured article. Click here for more information.");
		
		//Step 5: Take a screenshot of the right hand box that contains element properties
		WebElement table=driver.findElement(By.xpath("//table[@class='infobox']"));
		System.out.println("table"+table);
		File screenShot=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		BufferedImage fullimg=ImageIO.read(screenShot);
		Point point=table.getLocation();
		System.out.println("ponit"+point);
		int eleHeight=table.getSize().getHeight();
		System.out.println(eleHeight);
		int eleWidth=table.getSize().getWidth();
		System.out.println(eleWidth);
		BufferedImage eleScreenshot=fullimg.getSubimage(point.getX(),point.getY(),eleHeight,eleWidth);
		ImageIO.write(eleScreenshot,"png",screenShot);
		FileUtils.copyFile(screenShot,new File("test.png"));
		
		//Step 6: Count the number of pdf links in “References“
		driver.findElement(By.xpath("(//a//span[@class='toctext'])[32]")).click();
		WebElement reference=driver.findElement(By.xpath("(//span[@id='References'])/parent::h2//following-sibling::div[1]"));
		List<WebElement> list=reference.findElements(By.tagName("li"));
		System.out.println(list.size());
		int link=0;
		for(int i=0;i<list.size();i++)
		{
			String str1=list.get(i).getText();
			if(str1.contains("PDF"))
			{
				System.out.println(str1);
				link=link+1;
			}
				
		}
		System.out.println(link);
		
		//step 7: In the search bar on top right enter “pluto” and verify that the 2 nd suggestion is “Plutonium”
		Thread.sleep(3000);
		driver.findElement(By.xpath("//input[@id='searchInput']")).click();
		Thread.sleep(2000);
		driver.findElement(By.xpath("//input[@id='searchInput']")).sendKeys("pluto");
		String text=driver.findElement(By.xpath("(//a[@title='Plutonium'])[3]")).getText();
		Assert.assertEquals(text,"Plutonium","element not found");

	}
	@AfterMethod
	public void tearDown()
	{
		driver.quit();
		
	}

}
