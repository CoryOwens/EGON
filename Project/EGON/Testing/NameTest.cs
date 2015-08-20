using System;
using EGON;
using Microsoft.VisualStudio.TestTools.UnitTesting;

namespace Testing
{
    [TestClass]
    public class NameTest
    {
        [TestMethod]
        public void TestNameSetAndGetForm()
        {
            Name n = new Name();
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.form = "first last";
            string form = n.form;
            Assert.AreEqual("first last", form, "Returned form does not match set form. Expected: first last. Returned: " + form);
        }
        [TestMethod]
        public void TestNameFormProvidedConstructor()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            string form = n.form;
            Assert.AreEqual("first last", form, "Returned form does not match set form. Expected: first last. Returned: " + form);
        }
        [TestMethod]
        public void TestNamePutGetComponent()
        {
            Name n = new Name();
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory");
            string first = n.GetComponent("first");
            Assert.AreEqual("Cory", first, "Returned first does not match set first. Expected: Cory. Returned: " + first);
            n.PutComponent("last", "Owens");
            string last = n.GetComponent("last");
            Assert.AreEqual("Owens", last, "Returned first does not match set first. Expected: Owens. Returned: " + last);
            first = n.GetComponent("first");
            Assert.AreEqual("Cory", first, "Returned first does not match set first after setting last. Expected: Cory. Returned: " + first);
        }
        [TestMethod]
        public void TestNameGetFullName()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory");
            n.PutComponent("last", "Owens");
            string full = n.GetFullName();
            Assert.AreEqual("Cory Owens", full, "Returned full name does not match expected. Expected: Cory Owens. Returned: " + full);
        }
        public void TestNameGetFullNameWithSpaceInIname()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory");
            n.PutComponent("last", "Ryan Owens");
            string full = n.GetFullName();
            Assert.AreEqual("Cory Ryan Owens", full, "Returned full name does not match expected. Expected: Cory Ryan Owens. Returned: " + full);
        }

        [TestMethod]
        public void TestNameGetFullNameWithApostrophePunctuation()
        {
            Name n = new Name("first'last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory");
            n.PutComponent("last", "Owens");
            string full = n.GetFullName();
            Assert.AreEqual("Cory'Owens", full, "Returned full name does not match expected. Expected: Cory'Owens. Returned: " + full);
        }
        [TestMethod]
        public void TestNameGetFullNameWithApostropheInName()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Austin");
            n.PutComponent("last", "O'Neal");
            string full = n.GetFullName();
            Assert.AreEqual("Austin O'Neal", full, "Returned full name does not match expected. Expected: Austin O'Neal. Returned: " + full);
        }
        [TestMethod]
        public void TestNameGetFullNameWithPlusSign()
        {
            Name n = new Name("first+last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory");
            n.PutComponent("last", "Owens");
            string full = n.GetFullName();
            Assert.AreEqual("CoryOwens", full, "Returned full name does not match expected. Expected: CoryOwens. Returned: " + full);
        }
        [TestMethod]
        public void TestNameGetFullNameWithPlusSignInName()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Cory+Ryan");
            n.PutComponent("last", "Owens");
            string full = n.GetFullName();
            Assert.AreEqual("Cory+Ryan Owens", full, "Returned full name does not match expected. Expected: Cory+Ryan Owens. Returned: " + full);
        }
        [TestMethod]
        public void TestNameGetFullNameWithDash()
        {
            Name n = new Name("first maiden-last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Jane");
            n.PutComponent("maiden", "Doe");
            n.PutComponent("last", "Smith");
            string full = n.GetFullName();
            Assert.AreEqual("Jane Doe-Smith", full, "Returned full name does not match expected. Expected: Jane Doe-Smith. Returned: " + full);
        }
        [TestMethod]
        public void TestNameGetFullNameWithDashInName()
        {
            Name n = new Name("first last");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("first", "Jean-Luc");
            n.PutComponent("last", "Picard");
            string full = n.GetFullName();
            Assert.AreEqual("Jean-Luc Picard", full, "Returned full name does not match expected. Expected: Jean-Luc Picard. Returned: " + full);
        }
        [TestMethod]
        public void TestNameSuhail()
        {
            Name n = new Name("personal al-title ibn'father");
            Assert.AreNotEqual(null, n, "Default constructor returned null object.");
            n.PutComponent("personal", "Suhail");
            n.PutComponent("al", "al");
            n.PutComponent("title", "Rashid");
            n.PutComponent("ibn", "ibn");
            n.PutComponent("father", "Sabiq");
            string full = n.GetFullName();
            Assert.AreEqual("Suhail al-Rashid ibn'Sabiq", full, "Returned full name does not match expected. Expected: Suhail al-Rashid ibn'Sabiq. Returned: " + full);
        }
    }
}
