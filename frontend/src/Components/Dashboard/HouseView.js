import React from 'react';
import OutputConsole from './OutputConsole';

const HouseView = () => {
  return (
    <div className='flex flex-col h-full'>
      <h2 className='text-center my-4'>House View</h2>
      <div className='flex-grow relative'>
        <div className='absolute inset-0 m-4 bottom-20 border-2 border-dashed border-gray-400 flex justify-center items-center'>
          <span>...</span>
        </div>
      </div>
      <div className='p-4 bg-gray-200'>
        <h3 className="text-lg font-semibold pb-2">Output Console</h3>
        <OutputConsole />
      </div>
    </div>
  );
};

export default HouseView;
