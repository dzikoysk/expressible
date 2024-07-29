package expressible.tests;

import expressible.GenerateExpressible;

@GenerateExpressible(root = "lite.expressible")
public class Application {
    public static void main(String[] args) {
        lite.expressible.Result<String, String> result = lite.expressible.Result.ok("Test");
    }
}
