import React from "react";
import { Heading } from "@chakra-ui/react";

function HeadingMedium(props) {
  return (
    <Heading
      fontSize={"20px"}
      fontFamily={"Pretendard-SemiBold"}
      {...props}
    ></Heading>
  );
}

export default HeadingMedium;
