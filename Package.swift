
// swift-tools-version:5.3
import PackageDescription

let package = Package(
   name: "ApiFramework",
   platforms: [
     .iOS(.v14),
   ],
   products: [
      .library(name: "ApiFramework", targets: ["ApiFramework"])
   ],
   targets: [
      .binaryTarget(
         name: "ApiFramework",
         url: "https://github.com/nilupulsandeepa/ApiFrameworkLibrary/releases/download/v1/ApiFramework.xcframework.zip",
         checksum:"93c9cc3aa755bfe5025001102bcce9dfeed1c0bdf05219ef9447d50a13787973")
   ]
)
