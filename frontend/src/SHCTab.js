import React from 'react';
import { Tab, Tabs, TabList, TabPanel } from '@chakra-ui/react';

const SHCTab = () => {
  return (
    <Tabs>
      <TabList>
        <Tab>SHC</Tab>
      </TabList>
      <TabPanel>
        <p>Smart Home Controller content goes here...</p>
      </TabPanel>
    </Tabs>
  );
};

export default SHCTab;