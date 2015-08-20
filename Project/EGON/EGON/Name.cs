using System.Collections.Generic;
using System.Linq;

namespace EGON
{
    public class Name
    {
        private readonly Dictionary<string, string> components;
        public string form { get; set; }

        public Name() : this(null) { }

        public Name(string form)
        {
            this.components = new Dictionary<string, string>();
            this.form = form;
        }

        public string GetComponent(string key)
        {
            string component = "";
            components.TryGetValue(key, out component);
            return component;
        }

        public void PutComponent(string key, string value)
        {
            components.Add(key, value);
        }

        public string GetFullName()
        {
            string full = "";
            int lastcursor = 0;
            int cursor = 0;
            while (cursor < form.Length)
            {
                char c = form[cursor];
                if (c == ' ' || c == '-' || c == '\'' || c == '+')
                {
                    int length = cursor - lastcursor;
                    if (length != 0)
                    {
                        string key = form.Substring(lastcursor, length);
                        string component = "";
                        components.TryGetValue(key, out component);
                        full += component;
                    }
                    if (c != '+')
                    {
                        full += c;
                    }
                    cursor++;
                    lastcursor = cursor;
                }
                else
                {
                    cursor++;
                }
            }
            if (lastcursor != cursor)
            {
                int length = cursor - lastcursor;
                if (length != 0)
                {
                    string key = form.Substring(lastcursor, length);
                    string component = "";
                    components.TryGetValue(key, out component);
                    full += component;
                }
            }
            return full;
        }
    }
}
