import React from 'react';
import SHCTab from './SHCTab';

const HomePage = () => {
  return (
    <div className='flex'>
      <div className='w-1/4'>
        <SHCTab />
      </div>
      <div className='w-3/4'>
        {/* Content of the right side goes here */}
        <p>Right side content...</p>
      </div>
    </div>
  );
};

export default HomePage;