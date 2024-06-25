import { extendTheme } from "@chakra-ui/react";

export const customTheme = extendTheme({
  breakpoints: {
    base: "0em",
    sm: "30em",
    md: "48em",
    lg: "64em",
    xl: "80em",
    "2xl": "96em",
  },
  fonts: {
    heading: "Pretendard, sans-serif",
    body: "Pretendard, sans-serif",
  },
  styles: {
    global: {
      heading: {
        textColor: "blackAlpha.900",
        fontFamily: "Pretendard",
        fontWeight: 400,
        _dark: {
          textColor: "pink",
        },
      },
      body: {
        textColor: "blackAlpha.900",
        fontFamily: "Pretendard",
        fontWeight: 400,
        _dark: {
          textColor: "pink",
        },
      },
    },
  },
  components: {
    Heading: {
      baseStyle: {
        color: "blackAlpha.900",
        fontFamily: "Pretendard, sans-serif",
        fontWeight: 800,
        _dark: {
          color: "pink",
        },
      },
    },
    Button: {
      variants: {
        solid: {
          bgColor: "#D8B7E5",
          color: "blackAlpha.900",
          _hover: {
            bgColor: "#836091",
          },
          _dark: {
            bgColor: "#836091",
            _hover: {
              bgColor: "#D8B7E5",
            },
          },
        },
      },
    },
  },
});
