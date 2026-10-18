#if canImport(Testing)
import Testing
import Imageproc

@Suite("Imageproc Swift Export Tests")
struct ImageprocExportTests {
    @Test("Imageproc swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "Imageproc swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Imageproc

final class ImageprocExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Imageproc swift module imported cleanly")
    }
}
#endif
